<%@page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>메인페이지</title>
    <link rel ="stylesheet" href="maindraft1.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="./js/hyeongdon.js"></script>
    <script>
        $(function(){
            var $btHideObj;
            loginstatus();
            $("body>div>section>article").empty();
            var cp = 1;
            $.ajax ({
                url : "./lessonlist",
                method: "get",
                data: {"current_page" : cp},
                success: function(responseObj){
                    //서버에서 전송해준 배열 객체로 저장                     
                    var startPageValue = 0;
                    var totalPageValue = 0;
                    if(responseObj.status == undefined) {
                        console.log(responseObj);
                        $(responseObj).each(function(index, element){
                            startPageValue = element.start_page;
                            totalPageValue = element.total_page;
                            //article객체생성
                            var $articleObj = $('<article>');
                            var articleHtml = "<ul>";
                            articleHtml += '<li>';
                            articleHtml += "<img src='./images/th"+ element.lesson_id +".jpg' alt='th" + element.lesson_id +".jpg'>";
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.lesson_name;
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.lesson_total_amount;
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.targetpercent + "%";
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.lesson_diffdays + "일 남음";
                            articleHtml += "</li>";
                            
                            
                            articleHtml += "</ul>";
                            //    var idValue = getQuery 
                            //     data: {"lessonId" : idValue}
                            $articleObj.attr("class", element.lesson_id);
                            $articleObj.attr("onclick", "location.href='classdetail.jsp?lessonId=" + element.lesson_id+"'" );
                            $articleObj.html(articleHtml);

                            $("section.classname1").append($articleObj);
                        });
                    }
                    if(totalPageValue > cp){
                        var $btMoreObj = $('<input type="button" class="seemore" value="더보기" style="display:none;">'); //더보기 조건 관련해서 수정
                        console.log("더보기추가");
                        $("section.classname1").append($btMoreObj);
                        $btMoreObj.show();
                        console.log("if문 들어간후 cp값"+cp);
                    }
                    $("section.classname1").show();
                    $btHideObj = $btMoreObj;
                },
                error: function(jqXHR){
                        alert("오류 내용: "+jqXHR.status);
                }
            });
            $("section.classname1").on('click','input[class="seemore"]',function(event){
                $btHideObj.hide();
                cp += 1;
                $.ajax ({
                url : "./lessonlist",
                method: "get",
                data: {"current_page" : cp},
                success: function(responseObj){
                    //서버에서 전송해준 배열 객체로 저장                     
                    var startPageValue = 0;
                    var totalPageValue = 0;
                    if(responseObj.status == undefined) {
                        console.log(responseObj);
                        $(responseObj).each(function(index, element){
                            startPageValue = element.start_page;
                            totalPageValue = element.total_page;
                            //article객체생성
                            var $articleObj = $('<article>');
                            var articleHtml = "<ul>";
                            articleHtml += '<li>';
                            articleHtml += "<img src='./images/th"+ element.lesson_id +".jpg' alt='th" + element.lesson_id +".jpg'>";
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.lesson_name;
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.lesson_total_amount;
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.targetpercent + "%";
                            articleHtml += "</li>";
                            articleHtml += "<li>";
                            articleHtml += element.lesson_diffdays + "일 남음";
                            articleHtml += "</li>";
                            
                            
                            articleHtml += "</ul>";
                            //    var idValue = getQuery 
                            //     data: {"lessonId" : idValue}
                            $articleObj.attr("class", element.lesson_id);
                            $articleObj.attr("onclick", "location.href='classdetail.jsp?lessonId=" + element.lesson_id+"'" );
                            $articleObj.html(articleHtml);

                            $("section.classname1").append($articleObj);
                        });
                    }
                    if(totalPageValue >= cp){
                        var $btMoreObj = $('<input type="button" class="seemore" value="더보기" style="display:none;">'); //더보기 조건 관련해서 수정
                        console.log("더보기추가");
                        $("section.classname1").append($btMoreObj);
                        $btMoreObj.show();
                        console.log("if문 들어간후 cp값"+cp);
                    }
                    $("section.classname1").show();
                    $btHideObj = $btMoreObj;
                },
                error: function(jqXHR){
                        alert("오류 내용: "+jqXHR.status);
                }
            });
        });
        $("article.search>input[name=search]").keyup(function(e){if(e.keyCode == 13) search(); });
        let $menuObj = $("header>article>a");
        $menuObj.click(function(){
            let hrefValue = $(this).attr("href");
            switch(hrefValue) {
            case 'categorylist':
                $("div.bg").show();
                $("div.modal").empty();
                $.ajax({
                    url: hrefValue,
                    method: "get",
                    success: function(responseObj){ //data는 응답내용, 성공응답
                        if(responseObj.status == undefined) {
                            var $tableObj = $("<table>");
                            var arr = responseObj;
                            var ct = 100;
                            var tableData = '<tr>';
                            $(arr).each(function(index, element){
                                if(element.categoryId - ct < 10){
                                    if(element.categoryId%100 == 0){
                                        tableData += '<th>';
                                        tableData += element.categoryName;
                                        tableData += '</th>';
                                        tableData += '<tr>';
                                        tableData += '<td><input type="hidden" class="ctid" value="';
                                        tableData += element.categoryId;
                                        tableData += '">모든 ';
                                        tableData += element.categoryName;
                                        tableData += '</td>';
                                        tableData += '</tr>';
                                    }
                                    if(element.categoryId%100 != 0){
                                        tableData += '<td><input type="hidden" class="ctid" value="';
                                        tableData += element.categoryId;
                                        tableData += '">';
                                        tableData += element.categoryName;  
                                        tableData += "</td>";
                                    }                                	
                                ct += 100;
                                }
                            tableData += '</tr>';
                            tableData += '<tr>';
                            });
                            console.log(tableData);
                            tableData += '</tr>';
                            $tableObj.html(tableData);
                            $("div.modal").append($tableObj);
                        }
                        $("div.bg").toggleClass("hide");
                        var $categoryNameObj = $("div.modal td");
                        $categoryNameObj.click(function(event){
                            $("section.classname1").empty();
                            var categoryIdValue = $(event.target).find("input").val();
                            $.ajax({
                                url: "./categorylist",
                                method: "post",
                                data: {"categoryId": categoryIdValue},
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
                                        articleHtml += "<li>";
                                        articleHtml += element.lesson_name;
                                        articleHtml += "</li>";
                                        articleHtml += "<li>";
                                        articleHtml += element.lesson_total_amount;
                                        articleHtml += "</li>";
                                        articleHtml += "<li>";
                                        articleHtml += element.targetpercent + "%";
                                        articleHtml += "</li>";
                                        articleHtml += "<li>";
                                        articleHtml += element.lesson_diffdays + "일 남음";
                                        articleHtml += "</li>";
                                        
                                        
                                        articleHtml += "</ul>";
                                        //    var idValue = getQuery 
                                        //     data: {"lessonId" : idValue}
                                        $articleObj.attr("class", element.lesson_id);
                                        $articleObj.attr("onclick", "location.href='classdetail.jsp?lessonId=" + element.lesson_id+"'" );
                                        $articleObj.html(articleHtml);

                                        $("section.classname1").append($articleObj);

                                        });
                                        
                                    }
                                    $("div.bg").hide();
                                },
                                error: function(jqXHR){ //실패응답
                                    alert("AJAX요청응답 실패 : 에러코드=" + jqXHR.status);
                                } 
                            });
                        });
                    },
                    error: function(jqXHR){ //실패
                        alert("AJAX요청응답 실패 : 에러코드=" + jqXHR.status);
                    }
                });
                return false;
                break;
            case 'logout' :
                $.ajax({
                    url : hrefValue,
                    success: function(){
                        location.href = "http://localhost:8888/C4U/maindraft1.jsp";
                    },
                    error: function(jqXHR){ //실패
                        alert("AJAX요청응답 실패 : 에러코드=" + jqXHR.status);
                    }
                });
                return false;
                break;
            }
        });
    });
    </script>
</head>
<body>
    <div class="container">
        <header>
        <article class = "category"><a href="categorylist">카테고리</a></article>
        <article class = "classopen"><a href="C4UOpeningACourse.html" onclick="window.open(this.href, '_blank', 'width=1100, height=600'); return false;">강좌 개설하기</a></article>
        <article class = "logo"><a href="maindraft1.jsp">C4U</a></article>
        <article class = "search"><input type="text" placeholder = "강좌검색" name = "search"></article>

        <article class = "login"><a href="login.html">로그인</a></article>
        <article class = "join"><a href="signup1.0.html">회원가입</a></article>

        <article class = "mypage"><a href="mypage1.0.html">마이페이지</a></article>	
        <article class = "logout"><a href="logout">로그아웃</a></article>
        </header>
        
        <section class = "thebiggestbox">
            <article class = "pic"></article>
        </section>
        <section class="classname1"><!-- style="display:none;"-->

        </section>

    	    <div class="bg">
                <div class="modal">
                </div>
            </div>

        <footer>
            <div class="left">
                <a href="notice.html">Notice</a>
                <a href="C4UTermsAndConditions.html" onclick="window.open(this.href, '_blank', 'width=1100, height=600'); return false;">Terms and Condition</a>
                <a href="C4UPrivacyPolicy.html" onclick="window.open(this.href, '_blank', 'width=700, height=600'); return false;">Privacy Policy</a>
                <a href="#none">Contact US</a>
             </div> 
        </footer>
    </div>
</body>
</html>