<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>test</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script>
        	$(function(){
        		 let $menuObj = $("header>nav>ul>li>a");
                 $menuObj.click(function(){
                	 let hrefValue = $(this).attr("href");
                     $.ajax({
                        url: hrefValue, //요청URL
                        method: "get", //요청방식, method가 get이면 생략가능
                        success: function(responseObj){
                   			if(responseObj.status == undefined) {
                   				var arr = responseObj;
                   				$(arr).each(function(index, element){
                   					console.log(element.lesson_name);
                   				});
                   			}
                        },
                        error: function(jqXHR){ //실패응답
                            alert("AJAX요청응답 실패 : 에러코드=" + jqXHR.status);
                        }
                    });
                 });
                 
                 //$("header>nav>ul>li>a[href=lessonlist]").trigger("click");
        	});
        </script>
</head>
<body>
<header>
<nav>
<ul>
<li><a href="lessonlist">강좌목록</a></li>
<li><a href="categorylist">카테고리목록</a></li>
</ul>
</nav>
</header>
<section>
<article>
</article>
</section>
</body>
</html>