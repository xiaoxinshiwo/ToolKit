$(function() {

    /**
     * 一个班上两天
     *
     * A班 第一天两点下班
     *      第二天夜航（国内航班结束）
     * D班 第一天两点下班后去国际上副班 国内来接班才能下班
     *      第二天国内第一个下班
     * B班 第一天两点下班
     *      第二天早上国际下午国内晚上国际夜航（国际最后航班结束）
     * C班 早早班 第一天两点下班
     *      第二天国内第二个下班
     * 每个队有五条通道 以1 2 3 4 5 区分
     * 每条通道上完成一个ADBC之后在D班变换顺序
     * 下班也是1 2 3 4 5顺序
     * 例：D班国内第一个下班 要等D班的五条通道下完C班才能下班。
     * 2019年2月21日 D2 第一天 两点下班后去国际上副班 国内来接班才能下班。
     */

    // 2019-02-21,D,1,2
    // 2019-02-22,D,2,2
    // 2019-02-23,休息
    // 2019-02-24,B,1,2
    // 2019-02-25,B,2,2
    // 2019-02-26,休息
    // 2019-02-27,C,1,2
    // 2019-02-28,C,2,2
    // 2019-03-01,休息
    // 2019-03-02,A,1,2
    // 2019-03-03,A,2,2
    // 2019-03-04,休息
    // 2019-03-05,D,1,3
    // 2019-03-06,D,2,3
    // 2019-03-07,休息
    // 2019-03-08,B,1,3
    // 2019-03-09,B,2,3
    // 2019-03-10,休息
    // 2019-03-11,C,1,3
    // 2019-03-12,C,2,3
    // 2019-03-13,休息
    // 2019-03-14,A,1,3
    // 2019-03-15,A,2,3
    // 2019-03-16,休息
    // 2019-03-17,D,1,4
    // 2019-03-18,D,2,4
    // 2019-03-19,休息
    // 2019-03-20,B,1,4
    // 2019-03-21,B,2,4

    $("#dateTime").val(DateFormat.format(new Date(), 'yyyy-MM-dd'));
    $("#sample").val(localStorage.getItem('sampleWorkDayALL') || '2019-02-21,D,1,2');
    // 默认加载结果
    calc();
    $("#calc").click(function(){
        calc();
    });
    $("#submit").click(function(){
        let buttonText = $("#submit").val();
        if("设置" == buttonText){
            $("#sample").removeAttr("disabled");
            $("#submit").val("保存");
        }else{
            $("#sample").attr("disabled","disabled");
            $("#submit").val("设置");
            localStorage.setItem('sampleWorkDayALL',$("#sample").val());
            calc();
        }
    });

    /**
     * 按照上述规则计算班次
     */
    function calc() {
        // 下次更新的话仍需要以D班作为模板，因为D班是1 2 3 4 5 顺序的开始
        let sampleWorkDayALL = localStorage.getItem('sampleWorkDayALL') || '2019-02-21,D,1,2';

        let targetDate = new Date();
        let inputDate = $("#dateTime").val();
        if(inputDate !='' ){
            targetDate = new Date(inputDate);
        }
        targetDate = DateFormat.format(targetDate, 'yyyy-MM-dd');
        let sampleWorkDayStrs = sampleWorkDayALL.split(",");
        let sampleWorkDayDate = DateFormat.format(new Date(sampleWorkDayStrs[0]), 'yyyy-MM-dd');
        let sampleWorkDay = sampleWorkDayStrs[1];
        let sampleWorkDayOrder = sampleWorkDayStrs[2];
        let sampleWorkDayContent  = sampleWorkDayStrs[3];
        // 计算目标时间和样例时间的天数差

        let dayDiff = dateDiff(sampleWorkDayDate,targetDate);
        let orderStr = ['A-1','A-2','OFF','D-1','D-2','OFF','B-1','B-2','OFF','C-1','C-2','OFF'];

        let workDayCircle = parseInt(dayDiff/12);
        let workDayOrder = parseInt(dayDiff%12);

        let sampleWorkDayIndex = getArrayIndex(orderStr,sampleWorkDay+'-'+sampleWorkDayOrder);
        // 重组 orderStr
        let orderStr1 = orderStr.slice(0,sampleWorkDayIndex);
        let orderStr2 = orderStr.slice(sampleWorkDayIndex,orderStr.length);
        orderStr = orderStr2.concat(orderStr1);
        let targetWorkDayStr = orderStr[workDayOrder];

        let targetWorkDay = targetWorkDayStr.split("-")[0];
        let targetWorkDayOrder = targetWorkDayStr.split("-")[1];
        let targetWorkDayContent = parseInt(sampleWorkDayContent) + parseInt(workDayCircle);
        if(targetWorkDayContent > 5){
            targetWorkDayContent = parseInt(targetWorkDayContent % 5);
            targetWorkDayContent = targetWorkDayContent == 0 ? 1 : targetWorkDayContent;
        }
        let finalTargetWorkDayOrder='';
        switch (targetWorkDay) {
            case 'OFF':
                finalTargetWorkDayOrder = '休息';
                break;
            case 'A':
                if('1' == targetWorkDayOrder){
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('第一天   两点下班');
                }else{
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('第二天   夜航（国内航班结束）');
                }
                break;
            case 'D':
                if('1' == targetWorkDayOrder){
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('第一天   两点下班后去国际上副班 国内来接班才能下班');
                }else{
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('第二天   国内第一个下班');
                }
                break;
            case 'B':
                if('1' == targetWorkDayOrder){
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('第一天   两点下班');
                }else{
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('第二天   早上国际下午国内晚上国际夜航（国际最后航班结束');
                }
                break;
            case 'C':
                if('1' == targetWorkDayOrder){
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('早早班 第一天   两点下班');
                }else{
                    finalTargetWorkDayOrder = targetWorkDay.concat(targetWorkDayContent).concat('第二天   国内第二个下班');
                }
                break;
        }
        $("#result").val(finalTargetWorkDayOrder);
    }

    //参数格式：2006-12-18
    function dateDiff(dayBegin, dayEnd) {
        let dateSpan,dayNumber;
        dayBegin = Date.parse(dayBegin);
        dayEnd = Date.parse(dayEnd);
        dateSpan = dayEnd - dayBegin;
        dateSpan = Math.abs(dateSpan);
        dayNumber = Math.floor(dateSpan / (24 * 3600 * 1000));
        return dayNumber;
    };

    /**
     * 获取数组下标
     * @param array
     * @param element
     * @returns {*}
     */
    function getArrayIndex(array,element){
        var i = array.length;
        while (i--) {
            if (array[i] === element) {
                return i;
            }
        }
        return -1;
    }
});