$(function(){

    /**
     * 1、openTab
     */

    // ---------------------- openTab ----------------------

    /**
     * 打开新页面
     *
     * @param tabSrc
     * @param tabName
     * @param isCloseCurrent: 是否关闭当前页
     */
    window.openTab = function (tabSrc, tabName, isCloseCurrent) {
        // open tab
        if (window.parent.$.adminTab) {
            window.parent.$.adminTab.openTab({
                tabSrc: tabSrc,
                tabName: tabName
            }, isCloseCurrent)
        } else {
            if (isCloseCurrent) {
                window.open(tabSrc, '_self');
            } else {
                window.open(tabSrc, '_blank');
            }
        }
    }
	
});
