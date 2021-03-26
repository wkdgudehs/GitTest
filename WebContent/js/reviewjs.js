function showreview(obj) {
    console.log("test");
      var div = document.querySelector('table');
      console.log(obj);
    html = '';
      

      for (var i = 0; i < obj.length; i++) {
          if(obj[i].review_commend==1){
              html += '<tr><td>' + obj[i].review_content + '</td><td>' + obj[i].review_date
                      + '</td><td> ' + '추천' + '</td><td>' + obj[i].review_writer + '</td></tr>';
          }else{
              html += '<tr><td>' + obj[i].review_content + '</td><td>' + obj[i].review_date
                      + '</td><td> ' + '비추천' + '</td><td>' + obj[i].review_writer + '</td></tr>';
          }
      
      }

      console.log("전" + html);
      div.innerHTML += html;
      console.log("후" + html);
  }

  //추천선택 이벤트 리스너
  document.addEventListener('DOMContentLoaded', function(){
      const rateForms = document.querySelectorAll('.rating'); 
      rateForms.forEach(function(item){//클릭 이벤트 리스너 각각 등록
          item.addEventListener('click',function(e){
              let elem = e.target;
              if(elem.classList.contains('rate_radio')){
                  rating.setRate(elem.parentElement, parseInt(elem.value)); // setRate() 에 ".rating" 요소를 첫 번째 파라메터로 넘김
              }
          })
      });
   });


  //별점 마킹 모듈 프로토타입으로 생성
  function Rating(){};
  Rating.prototype.rate = 0;
  Rating.prototype.setRate = function(newrate){
      //별점 마킹 - 클릭한 별 이하 모든 별 체크 처리
      this.rate = newrate;
      //document.querySelector('.ratefill').style.width = parseInt(newrate * 60) + 'px';
      let items = document.querySelectorAll('.rate_radio');
      items.forEach(function(item, idx){
          if(idx < newrate){
              item.checked = true;
          }else{
              item.checked = false;
          }
      });
  }
   
  Rating.prototype.showMessage = function(type){//경고메시지 표시
      switch(type){
          case 'rate':
              //안내메시지 표시
              document.querySelector('.review_rating .warning_msg').style.display = 'block';
              //지정된 시간 후 안내 메시지 감춤
              setTimeout(function(){
                  document.querySelector('.review_rating .warning_msg').style.display = 'none';
              },1000);            
              break;
          case 'review':
              //안내메시지 표시
              document.querySelector('.review_contents .warning_msg').style.display = 'block';
              //지정된 시   간 후 안내 메시지 감춤
              setTimeout(function(){
                  document.querySelector('.review_contents .warning_msg').style.display = 'none';
              },1000);    
              break;
      }
  }

  let rating = new Rating();//추천 인스턴스 생성