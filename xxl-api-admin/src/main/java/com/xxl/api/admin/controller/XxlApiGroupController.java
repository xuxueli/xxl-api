package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiGroup;
import com.xxl.api.admin.core.model.XxlApiProject;
import com.xxl.api.admin.dao.IXxlApiGroupDao;
import com.xxl.api.admin.dao.IXxlApiProjectDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/group")
public class XxlApiGroupController {

	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;
	@Resource
	private IXxlApiGroupDao xxlApiGroupDao;

	@RequestMapping
	public String index(Model model, int productId, @RequestParam(required = false, defaultValue = "-1")  int groupId) {

		// 项目
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(productId);
		if (xxlApiProject == null) {
			throw new RuntimeException("系统异常，项目ID非法");
		}
		model.addAttribute("productId", productId);
		model.addAttribute("project", xxlApiProject);

		// 分组列表
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(productId);
		model.addAttribute("groupList", groupList);

		// 选中分组
		XxlApiGroup groupInfo = null;
		if (CollectionUtils.isNotEmpty(groupList)) {
			for (XxlApiGroup groupItem: groupList) {
				if (groupId == groupItem.getId()) {
					groupInfo = groupItem;
				}
			}
		}
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupInfo", groupInfo);

		// 分组下的，接口列表

		return "group/group.list";
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlApiGroup xxlApiGroup) {
		// valid
		if (StringUtils.isBlank(xxlApiGroup.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“分组名称”");
		}

		int ret = xxlApiGroupDao.add(xxlApiGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiGroup xxlApiGroup) {
		// exist
		XxlApiGroup existGroup = xxlApiGroupDao.load(xxlApiGroup.getId());
		if (existGroup == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，分组ID非法");
		}

		// valid
		if (StringUtils.isBlank(xxlApiGroup.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“分组名称”");
		}

		int ret = xxlApiGroupDao.update(xxlApiGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {

		// 分组下是否存在接口

		int ret = xxlApiGroupDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
