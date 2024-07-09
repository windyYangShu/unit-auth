package com.metric.auth.server.controller;

import com.metric.auth.common.log.Logger;
import com.metric.auth.common.model.CommonRes;
import com.metric.auth.common.model.PageData;
import com.metric.auth.common.utils.JacksonUtils;
import com.metric.auth.core.persistence.dto.SysDeptDTO;
import com.metric.auth.core.persistence.entity.SysAcl;
import com.metric.auth.core.persistence.entity.SysRole;
import com.metric.auth.core.persistence.entity.SysUser;
import com.metric.auth.server.controller.vo.*;
import com.metric.auth.server.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-09
 **/
@Api(tags = "基础业务管理")
@RestController
@RequestMapping("/api/v1")
public class ServiceController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysAclService sysAclService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysAclModuleService sysAclModuleService;
    @Autowired
    private SysDeptService sysDeptService;

    private static final Logger LOGGER = Logger.getLogger(ServiceController.class);

    @ApiOperation("用户注册")
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public CommonRes<Void> register(@RequestBody @Valid UserParam userParam){
        LOGGER.info("register, UserParam:{}", JacksonUtils.toJson(userParam));
        sysUserService.register(userParam);
        return CommonRes.success();
    }

    @ApiOperation("用户注册信息校验")
    @RequestMapping(value = "/user/register/valid", method = RequestMethod.POST)
    public CommonRes<Void> registerValid(@RequestBody SysUserReqParam sysUserParam){
        LOGGER.info("registerValid, sysUserParam:{}", JacksonUtils.toJson(sysUserParam));
        sysUserService.registerUserValid(sysUserParam);
        return CommonRes.success();
    }

    @ApiOperation("获取用户详情")
    @RequestMapping(value = "/user/detail/{userName}", method = RequestMethod.GET)
    public CommonRes<SysUser> getUserDetail(@PathVariable String userName){
        LOGGER.info("getUserInfo, userName:{}", userName);
        return CommonRes.success(sysUserService.getUserByName(userName));
    }

    @ApiOperation("获取用户列表")
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public CommonRes<PageData<SysUser>> getUserList(
            @RequestParam(required = false, defaultValue = "1") String page,
            @RequestParam(required = false, defaultValue = "10") String pageSize,
            @RequestBody List<PageOrder> pageOrders){
        LOGGER.info("getUserList, page:{}, pageSize:{}, orderItem:{}",
                page, pageSize, JacksonUtils.toJson(pageOrders));
        PageData<Void> pageData = new PageData();
        pageData.setPageSize(Integer.parseInt(pageSize));
        pageData.setPage(Long.parseLong(page));
        return CommonRes.success(sysUserService.getUserList(pageData, pageOrders));
    }

    @ApiOperation("更新用户")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public CommonRes<Void> updateUser(@RequestBody @Valid UserParam userParam){
        LOGGER.info("updateUser, ");
        sysUserService.updateUser(userParam);
        return CommonRes.success();
    }

    @ApiOperation("删除用户")
    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    public CommonRes<Void> deleteUser(@RequestParam List<Integer> ids){
        LOGGER.info("getUserInfo, ids:{}", JacksonUtils.toJson(ids));
        sysUserService.deleteById(ids);
        return CommonRes.success();
    }

    @ApiOperation("新增权限点")
    @RequestMapping(value = "/acl/create", method = RequestMethod.DELETE)
    public CommonRes<Void> createAcl(@RequestBody AclParam param){
//        LOGGER.info("getUserInfo, ids:{}", JacksonUtils.toJson(ids));
        sysAclService.createAcl(param);
        return CommonRes.success();
    }

    @ApiOperation("更新权限点")
    @RequestMapping(value = "/acl/update", method = RequestMethod.DELETE)
    public CommonRes<Void> updateAcl(@RequestBody AclParam param){
//        LOGGER.info("getUserInfo, ids:{}", JacksonUtils.toJson(ids));
        sysAclService.updateAcl(param);
        return CommonRes.success();
    }

    @ApiOperation("权限点列表")
    @RequestMapping(value = "/acl/list", method = RequestMethod.DELETE)
    public CommonRes<PageData<SysAcl>> getAclList(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "1") String page,
            @RequestParam(required = false, defaultValue = "10") String pageSize,
            @RequestBody List<PageOrder> pageOrders){
        LOGGER.info("getAclList, name:{}, type:{}, status:{}, page:{}, pageSize:{}, pageOrders:{}",
                name, type, status, page, pageSize, JacksonUtils.toJson(pageOrders));
        PageData<Void> pageData = new PageData();
        pageData.setPageSize(Integer.parseInt(pageSize));
        pageData.setPage(Long.parseLong(page));
        return CommonRes.success(sysAclService.getAclList(name, type, status, pageData, pageOrders));
    }

    @ApiOperation("删除权限点")
    @RequestMapping(value = "/acl/delete", method = RequestMethod.DELETE)
    public CommonRes<Void> deleteAcl(@RequestParam List<Integer> ids){
        LOGGER.info("getUserInfo, ids:{}", JacksonUtils.toJson(ids));
        sysAclService.deleteById(ids);
        return CommonRes.success();
    }

    @ApiOperation("创建角色")
    @RequestMapping(value = "/role/create", method = RequestMethod.POST)
    public CommonRes<Void> createRole(@RequestBody RoleParam roleParam) {
        sysRoleService.createRole(roleParam);
        return CommonRes.success();
    }

    @ApiOperation("检查角色")
    @RequestMapping(value = "/role/check", method = RequestMethod.POST)
    public CommonRes<Void> checkRole(@RequestBody RoleParam roleParam) {
        sysRoleService.checkRole(roleParam);
        return CommonRes.success();
    }

    @ApiOperation("更新角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public CommonRes<Void> updateRole(@RequestBody RoleParam roleParam) {
        sysRoleService.update(roleParam);
        return CommonRes.success();
    }

    @ApiOperation("角色列表")
    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public CommonRes<PageData<SysRole>> getRoleList(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "1") String page,
            @RequestParam(required = false, defaultValue = "10") String pageSize,
            @RequestBody List<PageOrder> pageOrders) {
        PageData<SysRole> pageData = new PageData();
        pageData.setPageSize(Integer.parseInt(pageSize));
        pageData.setPage(Long.parseLong(page));
        return CommonRes.success(sysRoleService.getRoleList(name, type, status, pageData, pageOrders));
    }

    @ApiOperation("创建权限模块")
    @RequestMapping(value = "/acl/module/create", method = RequestMethod.POST)
    public CommonRes<Void> createAclModule(@RequestBody SysAclModuleParam aclModuleParam) {
        sysAclModuleService.createAclModule(aclModuleParam);
        return CommonRes.success();
    }

    @ApiOperation("更新权限模块")
    @RequestMapping(value = "/acl/module/update", method = RequestMethod.POST)
    public CommonRes<Void> updateAclModule(@RequestBody SysAclModuleParam aclModuleParam) {
        sysAclModuleService.updateAclModule(aclModuleParam);
        return CommonRes.success();
    }

    @ApiOperation("权限模块列表")
    @RequestMapping(value = "/acl/module/list", method = RequestMethod.GET)
    public CommonRes<Void> getAclModuleList() {
        sysAclModuleService.getAclModuleList();
        return CommonRes.success();
    }

    @ApiOperation("删除权限模块")
    @RequestMapping(value = "/acl/module/delete", method = RequestMethod.DELETE)
    public CommonRes<Void> deleteAclModule(@RequestParam List<Integer> ids) {
        sysAclModuleService.deleteAclModule(ids);
        return CommonRes.success();
    }





    @ApiOperation("获取部门目录树")
    @RequestMapping(value = "/dept/tree", method = RequestMethod.GET)
    public CommonRes<List<SysDeptDTO>> getDeptTree() {
        LOGGER.info("getDeptTree");
        return CommonRes.success(sysDeptService.getDeptTree());
    }

    @ApiOperation("获取部门详情")
    @RequestMapping(value = "/dept/detail", method = RequestMethod.GET)
    public CommonRes<SysDeptDTO> getDeptDetail(@RequestParam Integer id) {
        LOGGER.info("getDeptTree id:{}", id);
        return CommonRes.success(sysDeptService.getDeptDetail(id));
    }

    // todo 还可以加入批量更新
    @ApiOperation("新增部门")
    @RequestMapping(value = "/dept/create", method = RequestMethod.POST)
    public CommonRes<Void> createDept(@RequestBody DeptParam deptParam){
        LOGGER.info("createDept, deptParam:{}", JacksonUtils.toJson(deptParam));
        sysDeptService.createDept(deptParam);
        return CommonRes.success();
    }

    @ApiOperation("更新部门")
    @RequestMapping(value = "/dept/update", method = RequestMethod.POST)
    public CommonRes<Void> updateDept(@RequestBody DeptParam deptParam){
        LOGGER.info("updateDept, deptParam:{}", JacksonUtils.toJson(deptParam));
        sysDeptService.updateDept(deptParam);
        return CommonRes.success();
    }

    @ApiOperation("批量更新部门所属")
    @RequestMapping(value = "/dept/update/belong", method = RequestMethod.PUT)
    public CommonRes<Void> updateDeptParent(@RequestBody BelongReqParam belongReqParam){
        LOGGER.info("updateDeptParent, belongReqParam:{}", JacksonUtils.toJson(belongReqParam));
        sysDeptService.updateDeptParent(belongReqParam);
        return CommonRes.success();
    }

    @ApiOperation("删除部门")
    @RequestMapping(value = "/dept/delete", method = RequestMethod.DELETE)
    public CommonRes<Void> deleteDept(@RequestParam List<Integer> ids){
        LOGGER.info("deleteDept, ids:{}", JacksonUtils.toJson(ids));
        sysDeptService.deleteById(ids);
        return CommonRes.success();
    }
}
