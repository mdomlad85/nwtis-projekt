var socket = new WebSocket("ws://localhost:16962/mdomladov_aplikacija_3_2/jms2");
socket.onmessage = onMessage;

function onMessage(event) {
    console.log(event.data);
    dodajPoruku([{name: 'poruka', value: event.data}]);
}