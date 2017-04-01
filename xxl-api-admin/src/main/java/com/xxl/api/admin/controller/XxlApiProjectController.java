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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/project")
public class XxlApiProjectController {

	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;
	@Resource
	private IXxlApiGroupDao xxlApiGroupDao;

	@RequestMapping
	public String index(Model model) {
		return "project/project.list";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
										@RequestParam(required = false, defaultValue = "10") int length,
										String name) {
		// page list
		List<XxlApiProject> list = xxlApiProjectDao.pageList(start, length, name);
		int list_count = xxlApiProjectDao.pageListCount(start, length, name);

		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count);		// 总记录数
		maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
		maps.put("data", list);  					// 分页列表
		return maps;
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlApiProject xxlApiProject) {
		// valid
		if (StringUtils.isBlank(xxlApiProject.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“项目名称”");
		}
		if (StringUtils.isBlank(xxlApiProject.getBaseUrlProduct())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“跟地址：线上环境”");
		}

		int ret = xxlApiProjectDao.add(xxlApiProject);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiProject xxlApiProject) {
		// exist
		XxlApiProject existProkect = xxlApiProjectDao.load(xxlApiProject.getId());
		if (existProkect == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，项目ID非法");
		}

		// valid
		if (StringUtils.isBlank(xxlApiProject.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“项目名称”");
		}
		if (StringUtils.isBlank(xxlApiProject.getBaseUrlProduct())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“跟地址：线上环境”");
		}

		int ret = xxlApiProjectDao.update(xxlApiProject);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {

		// 项目下是否存在分组
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(id);
		if (CollectionUtils.isNotEmpty(groupList)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "该项目下存在分组信息，拒绝删除");
		}

		int ret = xxlApiProjectDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
