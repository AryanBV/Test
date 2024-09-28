const fs = require('fs');

// Function to decode a number from a given base
function decode(base, value) {
    return parseInt(value, parseInt(base));
}

// Lagrange Interpolation to find the polynomial value at x=0 (which gives us c)
function lagrangeInterpolate(points) {
    let c = 0; // c is the y-intercept (f(0))
    for (let i = 0; i < points.length; i++) {
        let xi = points[i].x;
        let yi = points[i].y;

        let term = yi; // Initialize term for this y-value
        for (let j = 0; j < points.length; j++) {
            if (i !== j) {
                let xj = points[j].x;
                term *= (0 - xj) / (xi - xj); // Compute the term for Lagrange basis polynomial
            }
        }
        c += term; // Add the term contribution to c
    }
    return c;
}

// Read JSON input
const jsonFileContent = fs.readFileSync('input.json');
const data = JSON.parse(jsonFileContent);

// Extract the keys
const { n, k } = data.keys;

let points = [];

// Decode all provided points (using the actual keys in the input JSON)
Object.keys(data).forEach(key => {
    if (key !== 'keys') {  // Ignore the 'keys' object
        const point = data[key];
        const x = parseInt(key);  // The key represents the x-value
        const y = decode(point.base, point.value);  // Decode the y-value
        points.push({ x, y });
    }
});

// Ensure that we have at least 'k' points
if (points.length < k) {
    console.error("Not enough points to solve for the polynomial coefficients");
    process.exit(1);
}

// Calculate the secret (c)
const secret = lagrangeInterpolate(points);

// Output the constant term c
console.log(`The constant term c is: ${secret}`);
