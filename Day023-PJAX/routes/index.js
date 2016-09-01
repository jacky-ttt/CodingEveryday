var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/page1', function(req, res, next) {
  res.render('page1', { title: 'Page1' });
});

router.get('/page2', function(req, res, next) {
  res.render('page2', { title: 'Page2' });
});

module.exports = router;
