var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var multer = require('multer');
var xlsx = require('xlsx');
var pdfkit = require('pdfkit');
var fs = require('fs');


// var routes = require('./routes/index');
// var users = require('./routes/users');

var app = express();

// // view engine setup
// app.set('views', path.join(__dirname, 'views'));
// app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// app.use('/', routes);
// app.use('/users', users);


app.use('/js', express.static(__dirname + '/node_modules/bootstrap-filestyle/src'));

app.get('/', function (req, res) {
    res.sendFile(__dirname + "/views/index.html");
});

app.get('/downloadExcel', function (req, res) {
    var file = './public/abc.xlsx';
    res.download(file);
});

app.get('/downloadPdf', function (req, res) {
    var file = './public/file.pdf';
    res.download(file);
});

app.get('/firebase', function (req, res) {
    res.sendFile(__dirname + "/views/firebase.html");
});


var storage = multer.diskStorage({
    destination: function (req, file, callback) {
        callback(null, './public');
    },
    filename: function (req, file, callback) {
        callback(null, file.originalname);
    }
});
var upload = multer({storage: storage});
var cpUpload = upload.fields([
    {name: 'title', maxCount: 1},
    {name: 'excel', maxCount: 1},
    {name: 'image', maxCount: 1}
]);

app.post('/upload', function (req, res) {
    cpUpload(req, res, function (err) {
        if (err) {
            return res.end("Error uploading file." + err);
        }
        readExcel(req.files.excel);
        makePDF(req.body.title, req.files.excel, req.files.image, someText);
        res.sendFile(__dirname + "/views/result.html");
    });
});

function readExcel(excel) {
    var workbook = xlsx.readFile('./' + excel[0].path);

    var first_sheet_name = workbook.SheetNames[0];
    var address_of_cell = 'A1';

    /* Get worksheet */
    var worksheet = workbook.Sheets[first_sheet_name];

    /* Find desired cell */
    var desired_cell = worksheet[address_of_cell];

    /* Get the value */
    var desired_value = desired_cell.v;

    console.log(desired_value);
    console.log(xlsx.utils.sheet_to_json(worksheet));
    var json = xlsx.utils.sheet_to_json(worksheet);
    var p;
    for (p in json) {
        console.log(json[p]);
        console.log(json[p].product);
        console.log(json[p].price);
    }
    return json;
}

var someText = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam in suscipit purus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vivamus nec hendrerit felis. Morbi aliquam facilisis risus eu lacinia. Sed eu leo in turpis fringilla hendrerit. Ut nec accumsan nisl. Suspendisse rhoncus nisl posuere tortor tempus et dapibus elit porta. Cras leo neque, elementum a rhoncus ut, vestibulum non nibh. Phasellus pretium justo turpis. Etiam vulputate, odio vitae tincidunt ultricies, eros odio dapibus nisi, ut tincidunt lacus arcu eu elit. Aenean velit erat, vehicula eget lacinia ut, dignissim non tellus. Aliquam nec lacus mi, sed vestibulum nunc. Suspendisse potenti. Curabitur vitae sem turpis. Vestibulum sed neque eget dolor dapibus porttitor at sit amet sem. Fusce a turpis lorem. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae;\nMauris at ante tellus. Vestibulum a metus lectus. Praesent tempor purus a lacus blandit eget gravida ante hendrerit. Cras et eros metus. Sed commodo malesuada eros, vitae interdum augue semper quis. Fusce id magna nunc. Curabitur sollicitudin placerat semper. Cras et mi neque, a dignissim risus. Nulla venenatis porta lacus, vel rhoncus lectus tempor vitae. Duis sagittis venenatis rutrum. Curabitur tempor massa tortor.';

function makePDF(title, excel, image, defaultText) {
    var doc = new pdfkit();
    doc.pipe(fs.createWriteStream('./public/file.pdf'));

    // draw some text
    doc.fontSize(25)
        .text('Title: ' + title, 100, 80);

    // write text according to the uploaded excel
    var i = 0;
    var excelJson = readExcel(excel);
    var dynamicTextHeight = 0;
    for (p in excelJson) {
        doc.fontSize(20)
            .text('Product: ' + excelJson[p].product + ', Price: ' + excelJson[p].price, 100, 110 + i);
        i += 20;
    }
    dynamicTextHeight = 110 + i;

    doc.image('./' + image[0].path, 100, dynamicTextHeight, {fit: [100, 100]});
    dynamicTextHeight += 120;

    // and some justified text wrapped into columns
    doc.text('And here is some default text...', 100, dynamicTextHeight)
        .font('Times-Roman', 13)
        .moveDown()
        .text(defaultText, {
            width: 412,
            align: 'justify',
            indent: 30,
            columns: 2,
            height: 300,
            ellipsis: true
        });

    doc.end();
}


// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function (err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function (err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});


module.exports = app;
