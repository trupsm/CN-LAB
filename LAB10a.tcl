# Create a simulator object to control the entire simulation
set ns [new Simulator]

# Open a trace file to record all simulation events
set ntrace [open lab10.tr w]
$ns trace-all $ntrace

# Open a NAM file for graphical visualization
set namfile [open lab10.nam w]
$ns namtrace-all $namfile

# Define a finish procedure to execute at the end of simulation
proc Finish {} {
    global ns ntrace namfile

    # Flush all pending trace data
    $ns flush-trace

    # Close the trace file
    close $ntrace

    # Close the NAM file
    close $namfile

    # Launch the NAM animation
    exec nam lab10.nam &

    # Print message before showing packet drop count
    exec echo "The number of packets dropped is:"

    # Count and display dropped packets from trace file
    exec grep -c "^d" lab10.tr

    # Exit the simulator
    exit 0
}

# Create the source node
set n0 [$ns node]

# Create the intermediate router node
set n1 [$ns node]

# Create the destination node
set n2 [$ns node]

# Label the source node for NAM display
$n0 label "TCP Source"

# Label the router node for NAM display
$n1 label "Router"

# Label the destination node for NAM display
$n2 label "Sink"

# Set traffic class 1 color to blue in NAM
$ns color 1 blue

# Create a duplex link between source and router with DropTail queue
$ns duplex-link $n0 $n1 1Mb 10ms DropTail

# Create a duplex link between router and destination with DropTail queue
$ns duplex-link $n1 $n2 1Mb 10ms DropTail

# Set the visual orientation of the first link to the right
$ns duplex-link-op $n0 $n1 orient right

# Set the visual orientation of the second link to the right
$ns duplex-link-op $n1 $n2 orient right

# Set queue limit of 10 packets between source and router
$ns queue-limit $n0 $n1 10

# Set queue limit of 10 packets between router and destination
$ns queue-limit $n1 $n2 10

# Create a TCP agent for the source node
set tcp0 [new Agent/TCP]

# Attach the TCP agent to the source node
$ns attach-agent $n0 $tcp0

# Create a TCP sink agent for the destination node
set sink0 [new Agent/TCPSink]

# Attach the TCP sink agent to the destination node
$ns attach-agent $n2 $sink0

# Connect the TCP source and TCP sink
$ns connect $tcp0 $sink0

# Create a CBR application to generate traffic
set cbr0 [new Application/Traffic/CBR]

# Set packet size to 100 bytes
$cbr0 set packetSize_ 100

# Set data rate to 1 Mbps
$cbr0 set rate_ 1Mb

# Disable random packet transmission
$cbr0 set random_ false

# Attach the CBR application to the TCP agent
$cbr0 attach-agent $tcp0

# Assign traffic class for coloring in NAM
$tcp0 set class_ 1

# Schedule the CBR traffic to start at time 0 seconds
$ns at 0.0 "$cbr0 start"

# Schedule the finish procedure at time 5 seconds
$ns at 5.0 "Finish"

# Run the simulation
$ns run
