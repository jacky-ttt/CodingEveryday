var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');

var Pokeio = require('pokemon-go-node-api');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);


var a = new Pokeio.Pokeio();

//Set environment variables or replace placeholder text
var location = {
    type: 'name',
    name: 'Times Square'
};

var username = '<email>';
var password = '<password>';
var provider = 'google';

a.init(username, password, location, provider, function (err) {
    if (err) throw err;

    console.log('1[i] Current location: ' + a.playerInfo.locationName);
    console.log('1[i] lat/long/alt: : ' + a.playerInfo.latitude + ' ' + a.playerInfo.longitude + ' ' + a.playerInfo.altitude);

    a.GetProfile(function (err, profile) {
        if (err) throw err;

        console.log('1[i] Username: ' + profile.username);
        console.log('1[i] Poke Storage: ' + profile.poke_storage);
        console.log('1[i] Item Storage: ' + profile.item_storage);

        var poke = 0;
        if (profile.currency[0].amount) {
            poke = profile.currency[0].amount;
        }

        console.log('1[i] Pokecoin: ' + poke);
        console.log('1[i] Stardust: ' + profile.currency[1].amount);

        setInterval(function () {
            a.Heartbeat(function (err, hb) {
                if (err) {
                    console.log(err);
                }

                for (var i = hb.cells.length - 1; i >= 0; i--) {
                    if (hb.cells[i].NearbyPokemon[0]) {
                        //console.log(a.pokemonlist[0])
                        var pokemon = a.pokemonlist[parseInt(hb.cells[i].NearbyPokemon[0].PokedexNumber) - 1];
                        console.log('1[+] There is a ' + pokemon.name + ' at ' + hb.cells[i].NearbyPokemon[0].DistanceMeters.toString() + ' meters');
                    }
                }

            });
        }, 5000);

    });
});

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
        res.sendFile(path.join(__dirname, 'views', 'error.html'));
        // res.render('error', {
        //   message: err.message,
        //   error: err
        // });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function (err, req, res, next) {
    res.status(err.status || 500);
    res.sendFile(path.join(__dirname, 'views', 'error.html'));
    // res.render('error', {
    //   message: err.message,
    //   error: {}
    // });
});


module.exports = app;
