function search(){
    var union = $("article.search>input[name=search]").val();
    $("section.classname1").empty();
    $("section.mypagesection").empty();
    $("section.noticesection").empty();
    $("nav").remove();
    $.ajax({
        url : "http://192.168.0.220:8888/C4U/lessonlist",
        method: "post",
        data: {"union" : union},
        success: function(responseObj){
            if(responseObj.status == undefined) {
                console.log(responseObj);
                $(responseObj).each(function(index, element){
                        //article객체생성
                        var $articleObj = $('<article>');
                        var articleHtml = "<ul>";
                        articleHtml += '<li>';
                        articleHtml += "<img src='./images/th"+ element.lesson_id +".jpg' alt='th" + element.lesson_id +".jpg'>";
                        articleHtml += "</li>";
                        articleHtml += "<li><b>&nbsp;&nbsp;&nbsp;";
                        articleHtml += element.lesson_name;
                        articleHtml += "</b></li>";
                        articleHtml += "<li>&nbsp;&nbsp;&nbsp;&nbsp;";
                        articleHtml += element.lesson_total_amount;
                        articleHtml += "</li>";
                        articleHtml += "<li>&nbsp;&nbsp;&nbsp;&nbsp;";
                        articleHtml += element.targetpercent + "%";
                        articleHtml += "</li>";
                        articleHtml += "<li>&nbsp;&nbsp;&nbsp;&nbsp;";
                        articleHtml += element.lesson_diffdays + "일 남음";
                        articleHtml += "</li>";
                        
                        
                        articleHtml += "</ul>";
                        //    var idValue = getQuery 
                        //     data: {"lessonId" : idValue}
                        $articleObj.attr("class", element.lesson_id);
                        $articleObj.attr("onclick", "location.href='classdetail.html?lessonId=" + element.lesson_id+"'" );
                        $articleObj.html(articleHtml);

                        $("section.classname1").append($articleObj);
                        console.log(element.lesson_name);
                        console.log(element.lesson_id);
                    });                    
            }else if (responseObj.status == -1){
                console.log(responseObj.status);
                var $searchObj = $('<article>');
                var searchHtml = "<h3>검색결과가 없습니다</h3>";
                $searchObj.html(searchHtml);
                $searchObj.css("background-color","white");
                $("section.classname1").append($searchObj);
            }
        },
    });
}

function loginstatus(){
	$.ajax({
		url: "./loginstatus", //서블릿 만들어야함
		method: "get",
		success:function(responseObj){
			var status = responseObj.status;
			//var logined = responseObj.loginInfo;
			if(status == 1) {
				$("header>article.mypage").show();
				$("header>article.logout").show();				
				$("header>article.login").hide();				
				$("header>article.join").hide();
			}else{
				$("header>article.mypage").hide();
				$("header>article.logout").hide();
				$("header>article.login").show();
				$("header>article.join").show();
			}
		}
	});
	//var loginedId = localStorage.getItem("loginInfo");
	//if(loginedId)
}
function getQueryString(key) {
 
    // 전체 Url을 가져온다.
    var str = location.href;
 
    // QueryString의 값을 가져오기 위해서, ? 이후 첫번째 index값을 가져온다.
    var index = str.indexOf("?") + 1;
 
    // Url에 #이 포함되어 있을 수 있으므로 경우의 수를 나눴다.
    var lastIndex = str.indexOf("#") > -1 ? str.indexOf("#") + 1 : str.length;
 
    // index 값이 0이라는 것은 QueryString이 없다는 것을 의미하기에 종료
    if (index == 0) {
        return "";
    }
 
    // str의 값은 a=1&b=first&c=true
    str = str.substring(index, lastIndex); 
 
    // key/value로 이뤄진 쌍을 배열로 나눠서 넣는다.
    str = str.split("&");
 
    // 결과값
    var rst = "";
 
    for (var i = 0; i < str.length; i++) {
 
        // key/value로 나눈다.
        // arr[0] = key
        // arr[1] = value
        var arr = str[i].split("=");
 
        // arr의 length가 2가 아니면 종료
        if (arr.length != 2) {
            break;
        }
 
        // 매개변수 key과 일치하면 결과값에 셋팅
        if (arr[0] == key) {
            rst = arr[1];
            break;
        }
    }
    return rst;
}