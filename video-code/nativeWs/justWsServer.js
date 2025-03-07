//http is core node module
const http = require('http');
//ws is third party module
const websocket = require('ws');

let count = 1

const server = http.createServer((req,res) => {
    res.end(count + ': I am connected');
    count = count + 1
});

const wss = new websocket.WebSocketServer({
    server
})

wss.on('headers', (headers,req) => {
    console.log(headers)
})

wss.on('connection',(ws1,req)=> {
    ws1.send(count + ': Welcome Ditya to the WebSocketServer !!!')
    ws1.on('message', msg => {
        console.log(msg.toString())
    })
    count = count + 1
})


server.listen(8000);
