/*
Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
For licensing, see license.txt or http://cksource.com/ckfinder/license
*/

CKFinder.customConfig = function( config )
{
	// Define changes to default configuration here.
	// For the list of available options, check:
	// http://docs.cksource.com/ckfinder_2.x_api/symbols/CKFinder.config.html

	//界面颜色
	config.uiColor = '#f7f5f4';
	//语言
	config.language = 'zh-cn';
	//移除插件
	config.removePlugins = 'basket,help';
	//默认排序(根据日期)
	config.defaultSortBy = 'date';
};
