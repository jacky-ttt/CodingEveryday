function removeFromArray(arr, elt) {
	// looping from the back
	// if it is looping forwards, when an element is removed from the array, 
	// an element would be skipped in the process. 
	for (var i = arr.length - 1; i >= 0; i--) {
		if (arr[i] == elt) {
			arr.splice(i, 1);
		}
	}
}

// calculate Euclidean distance between two spots
function heuristic(a, b) {
	// var d = dist(a.i, a.j, b.i, b.j);
	var d = abs(a.i - b.i) + abs(b.i - b.j);
	return d;
}


var grid = new Array(cols);
var cols = 10,
	rows = 10;

var openSet = [];
var closedSet = [];
var start;
var end;
var w, h;
var path = [];

function Spot(i, j) {
	this.i = i;
	this.j = j;
	this.f = 0;
	this.g = 0;
	this.h = 0;
	this.neighbors = [];
	this.previous = undefined;

	this.show = function(color) {
		fill(color);
		noStroke();
		rect(this.i * w, this.j * h, w - 1, h - 1);
	}

	this.addNeighbors = function(grid) {
		var i = this.i;
		var j = this.j;

		if (i < cols - 1)
			this.neighbors.push(grid[i + 1][j]);
		if (i > 0)
			this.neighbors.push(grid[i - 1][j]);
		if (j < rows - 1)
			this.neighbors.push(grid[i][j + 1]);
		if (j > 0)
			this.neighbors.push(grid[i][j - 1]);
	}
}

function setup() {
	createCanvas(400, 400);
	console.log('A*');

	w = width / cols;
	h = height / rows;

	//making a 2d array
	for (var i = 0; i < cols; i++) {
		grid[i] = new Array(rows);
	}

	for (var i = 0; i < cols; i++) {
		for (var j = 0; j < rows; j++) {
			grid[i][j] = new Spot(i, j);
		}
	}

	for (var i = 0; i < cols; i++) {
		for (var j = 0; j < rows; j++) {
			grid[i][j].addNeighbors(grid);
		}
	}

	start = grid[0][0];
	end = grid[cols - 1][rows - 1];

	openSet.push(start);

	console.log(grid);
	console.log(openSet);
}

function draw() {
	if (openSet.length > 0) {
		// we can keep going

		var winner = 0;
		for (vari = 0; i < openSet.length; i++) {
			if (openSet[i].f < openSet[winner].f) {
				winner = i;
			}
		}

		var current = openSet[winner];

		if (current === end) {
			noLoop();
			console.log('done!');

		}

		removeFromArray(openSet, current);
		closedSet.push(current);

		var neighbors = current.neighbors;
		for (var i = 0; i < neighbors.length; i++) {
			var neighbor = neighbors[i];

			if (!closedSet.includes(neighbor)) {
				var tempG = current.g + 1;


				if (openSet.includes(neighbor)) {
					// if it costs less than the original, there exist a path with lower cost to get there. 
					if (tempG < neighbor.g) {
						neighbor.g = tempG;
					}
				} else {
					// if that spot is neither in closedSet and openSet, the spot has not been discovered yet. 
					// simply use the tempG as value. 
					neighbor.g = tempG;

					// neighbor is not yet in the openSet, add it
					openSet.push(neighbor);
				}

				// make an educated guess of how much cost does it take 
				// to go to the end from neighbor spot
				neighbor.h = heuristic(neighbor, end);

				neighbor.f = neighbor.g + neighbor.h;

				neighbor.previous = current;
			}
		}
	} else {
		// no solution
	}

	background(0);

	for (var i = 0; i < cols; i++) {
		for (var j = 0; j < rows; j++) {
			grid[i][j].show(color(255));
		}
	}

	for (var i = 0; i < closedSet.length; i++) {
		closedSet[i].show(color(255, 0, 0));
	}

	for (var i = 0; i < openSet.length; i++) {
		openSet[i].show(color(0, 255, 0));
	}


	// find the path
	path = [];
	var temp = current;
	path.push(temp);
	while (temp.previous !== undefined) {
		path.push(temp.previous);
		temp = temp.previous;
	}

	for (var i = 0; i < path.length; i++) {
		path[i].show(color(0, 0, 255));
	}
}