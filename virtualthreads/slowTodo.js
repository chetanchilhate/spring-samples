const http = require("http");

const server = http.createServer((req, res) => {
  res.writeHead(200, { "Content-Type": "application/json" });
  var date = new Date();
  var seconds = date.getSeconds();
  var milliSeconds = date.getMilliseconds();
  var id = (seconds * 1000) + milliSeconds

  res.write(`{"id": ${id},`);
  setTimeout(function() {
          res.end('"todo": "prepare virtual thread demo"}');
      }, 2000);
  
});

server.listen(3000, () => {
  console.log("Server running at http://localhost:3000/slow-todo");
});