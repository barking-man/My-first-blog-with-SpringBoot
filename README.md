# 2021.7.20
问题：
- 使用semantic-ui中的popup组件时，未能理解callback如何调用
- 用户注册界面，若使用button提交，则会报500错误，web控制台将提示localhost:8080/login  500错误

补充：
- 使用document设置具体元素的style属性
  ```javascript
  document.getElementById("errorDiv").style.display="none";
  ```
- input标签中，如果不输入任何值而提交表单，则后台接收到的将是""