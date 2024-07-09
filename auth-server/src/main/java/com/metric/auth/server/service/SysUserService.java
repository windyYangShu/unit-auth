package com.metric.auth.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.metric.auth.common.exceptions.BadRequestException;
import com.metric.auth.common.log.Logger;
import com.metric.auth.common.model.PageData;
import com.metric.auth.common.model.UserRoleConstants;
import com.metric.auth.common.utils.BeanCopierUtils;
import com.metric.auth.core.entites.SysJwtUser;
import com.metric.auth.core.persistence.dao.SysUserDao;
import com.metric.auth.core.persistence.entity.SysRole;
import com.metric.auth.core.persistence.entity.SysUser;
import com.metric.auth.core.persistence.mapper.SysRoleMapper;
import com.metric.auth.core.persistence.mapper.SysRoleUserMapper;
import com.metric.auth.core.util.JwtUtils;
import com.metric.auth.core.util.SpringSecurityUtil;
import com.metric.auth.server.controller.vo.PageOrder;
import com.metric.auth.server.controller.vo.SysUserLoginParam;
import com.metric.auth.server.controller.vo.SysUserReqParam;
import com.metric.auth.server.controller.vo.UserParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-06-29
 **/
@Service
public class SysUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleUserMapper roleUserMapper;

    private static final Logger LOGGER = Logger.getLogger(SysUserService.class);

    private static final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    private static final String PAGE_ORDER_ASC = "asc";

    private static final String KEY_WORD = "metric";

    public SysUser getUserByName(String userName) {
        return Optional
                .ofNullable(sysUserDao.getBaseMapper().getUserByName(userName)).orElse(new SysUser());
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(UserParam userParam) {
        registerUserValid(userParam);
        SysUser user = new SysUser();
        BeanCopierUtils.copy(userParam, user);
        // 将登录密码进行加密
        String cryptPassword = bCryptPasswordEncoder.encode(userParam.getPassword());
        user.setPassword(cryptPassword);
        sysUserDao.getBaseMapper().insert(user);
    }

    public void registerUserValid(UserParam userParam) {
        validUserName(userParam.getUsername());
        validTelephone(userParam.getTelephone());
        validMail(userParam.getMail());
        validPassword(userParam.getPassword(), userParam.getRePassword());
    }

    public void registerUserValid(SysUserReqParam userReqParam) {
        validUserName(userReqParam.getUsername());
        validTelephone(userReqParam.getTelephone());
        validMail(userReqParam.getMail());
        validPassword(userReqParam.getPassword(), userReqParam.getRePassword());
    }

    private void validPassword(String password, String rePassword) {
        if (StringUtils.isNotEmpty(password)) {
            // todo 测试时先屏蔽
//            String rawPassword = EncryptUtils.decryptAES(password, EncryptUtils.encryptMD5(KEY_WORD));
//            if (!rawPassword.matches(regex)) {
//                throw new BadRequestException("密码不符合规范");
//            }
        }
        if (StringUtils.isNotEmpty(rePassword) && !rePassword.equals(password)) {
            throw new BadRequestException("两次密码输入不一致");
        }
    }

    private void validMail(String mail) {
        if (StringUtils.isNotEmpty(mail) && sysUserDao.getBaseMapper().getUserByMail(mail) != null) {
            throw new BadRequestException("邮箱已被占用");
        }
    }

    private void validTelephone(String telephone) {
        if (StringUtils.isNotEmpty(telephone) && sysUserDao.getBaseMapper().getUserByTelephone(telephone) != null) {
            throw new BadRequestException("手机号已被占用");
        }
    }

    private void validUserName(String userName) {
        if (StringUtils.isNotEmpty(userName) && sysUserDao.getBaseMapper().getUserByName(userName) != null) {
            throw new BadRequestException("用户名已被占用");
        }
    }

    public PageData<SysUser> getUserList(PageData<Void> pageData, List<PageOrder> itemList) {
        Page<SysUser> page = new Page<SysUser>()
                .setCurrent(pageData.getPage())
                .setSize(pageData.getPageSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        if (CollectionUtils.isNotEmpty(itemList)) {
            itemList.stream()
                    .filter(m -> StringUtils.isNotEmpty(m.getColumn()))
                    .forEach(i -> {
                        if (StringUtils.isNotEmpty(i.getOrder())
                                && i.getOrder().toLowerCase().equalsIgnoreCase(PAGE_ORDER_ASC)) {
                            queryWrapper.orderByDesc(i.getColumn());
                        } else {
                            queryWrapper.orderByAsc(i.getColumn());
                        }
                    });
        }

        Page<SysUser> result = sysUserDao.page(page, queryWrapper);
        PageData<SysUser> res = new PageData();
        res.setData(result.getRecords());
        res.setTotal(result.getTotal());
        res.setPage(result.getCurrent());
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(List<Integer> ids) {
        sysUserDao.getBaseMapper().deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserParam userParam) {
        // todo 更新用户时的重复检查与注册时的不一样，比如传递过来的邮箱，不应该直接查重的应该先比较有没修改再查重
//        registerUserValid(userParam);
        SysUser user = sysUserDao.getBaseMapper().selectById(userParam.getId());
        Preconditions.checkNotNull(user, "该用户不存在");
        BeanCopierUtils.copy(userParam, user);
        user.setOperator(getAuthInfo());
        sysUserDao.updateById(user);
    }

    //todo 这个方法需要优化
    public SysJwtUser authLogin(SysUserLoginParam loginParam) {
        SysUser sysUser = null;
        if (loginParam.getKeyType().equalsIgnoreCase("username")) {
            sysUser = sysUserDao.getBaseMapper().getUserByName(loginParam.getKey());
        } else if (loginParam.getKeyType().equalsIgnoreCase("mail")) {
            sysUser = sysUserDao.getBaseMapper().getUserByMail(loginParam.getKey());
        } else if (loginParam.getKeyType().equalsIgnoreCase("telephone")){
            sysUser = sysUserDao.getBaseMapper().getUserByTelephone(loginParam.getKey());
        }
        if (sysUser == null) {
            throw new BadRequestException("用户不存在");
        }
//        if (this.bCryptPasswordEncoder.matches(loginParam.getPassword(), sysUser.getPassword())) {
        if (loginParam.getPassword().equals(sysUser.getPassword())) {
            List<SysRole> roleList  = roleMapper.getRoleByUser(sysUser.getId());
            List<String> roles = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(roleList)) {
                roles = roleList.stream().map(SysRole::getName).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(roles)) {
                roles = Collections.singletonList(UserRoleConstants.ROLE_USER);
            }
            // 生成 token
            String token = JwtUtils.generateToken(sysUser.getUsername(), roles, loginParam.getRememberMe());
            // 认证成功后，设置认证信息到 Spring Security 上下文中
            Authentication authentication = JwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new SysJwtUser(token, sysUser);
        }
        return null;
    }

    public void authLogout() {
        SecurityContextHolder.clearContext();
    }

    private String getAuthInfo() {
        Optional<String> userLogin = SpringSecurityUtil.getCurrentUserLogin();
            if (userLogin.isPresent()) {
            return userLogin.get();
        }
        return null;
    }

//    private Subject getAuthInfo() {
//        Optional<Object> opt = ThreadLocalContext.get(ThreadLocalContext.AUTH_SUBJECT);
//        if (opt.isPresent() && (opt.get() instanceof Subject)) {
//            return (Subject) opt.get();
//        } else {
//            throw new GlobalException("无法获取客户端认证信息");
//        }
//    }

//    private String getAccessUser() {
//        Subject authInfo = getAuthInfo();
//        return authInfo.getId();
//    }
//
//    private String getAuthorization() {
//        Subject authInfo = getAuthInfo();
//        return authInfo.getAuthentication();
//    }
//
//    private String getProxyUser() {
//        Optional<Object> proxySubOpt = ThreadLocalContext.get(AuthFilter.PROXY_USER);
//        if (proxySubOpt.isPresent()) {
//            return ((String) proxySubOpt.get());
//        } else {
//            return org.apache.commons.lang3.StringUtils.EMPTY;
//        }
//    }
}
