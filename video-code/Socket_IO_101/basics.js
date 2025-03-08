//3rd party module express.
//npm insstall express

const express = require('express');
const app = express();


app.use(express.static(__dirname + '/public'));

//npm install socket.io
const socketio = require('socket.io');

const expressServer = app.listen(8000);
const io = socketio(expressServer);
let count = 1;

io.on('connection', (socket) => {
    console.log(socket.id, ' has connected')
    socket.emit('msgFromSrvr', { data: 'Welcome to the server' })

    socket.on('msgFromClient', (d) => {
        console.log(d.data);
    })

})
