#!/usr/local/bin/gnuplot --persist

# read command line arguments
script = ARG0
resultsPath = ARG1
problem = ARG2

# check whether dataset file doesn't exist
resultsBaseline = resultsPath . problem . "-baseline.csv"
if (!exists("resultsBaseline")) {
    print "Unable to find results file: ", resultsBaseline
    exit
}
resultsSmp = resultsPath . problem . "-smp-4.csv"
if (!exists("resultsSmp")) {
    print "Unable to find results file: ", resultsSmp
    exit
}
resultsAws = resultsPath . problem . "-aws-4.csv"
if (!exists("resultsAws")) {
    print "Unable to find results file: ", resultsAws
    exit
}

print "Running ", script, " over ", problem, " results..."

# disable terminal output
set terminal unknown

set datafile separator ','
set key autotitle columnhead

# plot data to get x and y spans
plot resultsBaseline
plot resultsSmp
plot resultsAws
xspan = GPVAL_DATA_X_MAX - GPVAL_DATA_X_MIN
yspan = GPVAL_DATA_Y_MAX - GPVAL_DATA_Y_MIN

# define axis units
xequiv = 1024
yequiv = 1 # 2 for LCA graph

# aspect ratio
ar = yspan/xspan * xequiv/yequiv

# plot dimensions
ydim = 800
xdim = ydim/ar

# set x and y tic intervals
set xtics 500 rotate by -45
set ytics 500

# set x and y ranges
set xrange [0:GPVAL_DATA_X_MAX+GPVAL_DATA_X_MIN]
#set yrange [0:GPVAL_DATA_Y_MAX+GPVAL_DATA_Y_MIN]

# format y labels
#set format y "%.2f"

# set plot title and labels
plotTitle = problem
set title plotTitle font ",32" tc rgb "#606060"
set key left box autotitle columnhead
set xlabel "No. of graphs"
set ylabel "Time (s)"

# grid style
set style line 1 lw 0.5 lc rgb "#C0C0C0"
set grid ls 1

# graph style
set style data lines
set style line 2 lw 2 lc rgb "red"# dt "_"
set style line 22 lw 0 pt "×"
set style line 3 lw 2 lc rgb "#26DFD0"
set style line 33 lw 0 pt "¤"
set style line 4 lw 2 lc rgb "blue"
set style line 44 lw 0 pt "+"

# plot provided data with specified settings
plot resultsBaseline ls 2 title "Baseline", \
    "" with linespoints ls 22 notitle, \
    "" using 1:2:(sprintf("%.2f", $2)) with labels offset char 1, 1 notitle, \
    resultsSmp ls 3 title "SMP-4", \
    "" with linespoints ls 33 notitle, \
    "" using 1:2:(sprintf("%.2f", $2)) with labels offset char 1, 1 notitle, \
    resultsAws ls 4 title "AWS-4", \
    "" with linespoints ls 44 notitle, \
    "" using 1:2:(sprintf("%.2f", $2)) with labels offset char 1, 1 notitle

# save plot to image
plotImageName = resultsPath . "results-" . problem . ".png"
print "Saving plot to " . plotImageName . " as a " . sprintf("%d", xdim) . "x" \
    . sprintf("%d", ydim) . " image..."
set terminal pngcairo nocrop enhanced font "Lato-Medium,18" size xdim,ydim
set output plotImageName
set size ratio ar

replot
