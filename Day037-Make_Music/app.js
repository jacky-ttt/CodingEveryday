const scribble = require('scribbletune');
let clip = scribble.clip({
    notes: ['c4'],
    pattern: 'x-x-x-x-x-x-x-x-',
});
scribble.midi(clip);
