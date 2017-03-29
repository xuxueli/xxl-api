package com.xxl.api.admin.service.impl;


import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.service.IXxlApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class XxlApiServiceImpl implements IXxlApiService {
	private static Logger logger = LoggerFactory.getLogger(XxlApiServiceImpl.class);

	@Override
	public ReturnT<String> add() {
		return ReturnT.SUCCESS;
	}

}
