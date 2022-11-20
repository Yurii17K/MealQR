const express = require("express");
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());
app.use(express.static(__dirname));
 
app.get("/", function (req, res) {
    res.sendFile(__dirname + "/LogIn/LogIn.html");
});
 
app.post("/api/users/sign-in", function (req, res) {
    console.log(req.body.userEmail);
    console.log(req.body.userPass);
});

app.post("/api/users/sign-up", function (req, res) {
    console.log(req.body.email);
    console.log(req.body.name);
    console.log(req.body.lastName);
    console.log(req.body.city);
    console.log(req.body.pass);
    console.log(req.body.client);
    //res.redirect('/Cards/Cards.html');
});

app.post("/api/users/update-allergies", function (req, res) {
    console.log(req.body.allergies);
});
 
app.listen(3000, function () {
    console.log("Server started on port 3000");
});