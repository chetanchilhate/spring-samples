const http = require("http");

const server = http.createServer((req, res) => {
  res.writeHead(200, { "Content-Type": "application/json" });

  res.write('{"id": 1,');
  setTimeout(function() {
          res.end('  "todo": "prepare virtual thread demo"}');
      }, 2000);
  
});

server.listen(3000, () => {
  console.log("Server running at http://localhost:3000/slow-todo");
});