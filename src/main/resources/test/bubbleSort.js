var a = [];
a[0] = 34;
a[1] = 203;
a[2] = 3;
a[3] = 746;
a[4] = 200;
a[5] = 984;
a[6] = 198;
a[7] = 764;
a[8] = 9;

var i = 0;

var swapped = true;
while (swapped) {
    swapped = false;
    i = 0;
    while (i < 8) {//todo lenght of array
        if (a[i] > a[i+1]) {
            var temp = a[i];
            a[i] = a[i+1];
            a[i+1] = temp;
            swapped = true;
        }
        i = i + 1;
    }
}

console.log(a);

