# NS-2 Program: Study of Ping Packet Drops due to TCP/CBR Congestion

# Create the Simulator object
# This object controls the entire simulation
set ns [new Simulator]

# Define colors for different traffic classes (used only for NAM visualization)
# Class 1 is used for Ping traffic and shown in Blue
# Class 2 is used for TCP/CBR traffic and shown in Red
$ns color 1 Blue
$ns color 2 Red

# Open trace file to record all packet events such as send, receive and drop
set ntrace [open lab11.tr w]
$ns trace-all $ntrace

# Open NAM file for network animation
set namfile [open lab11.nam w]
$ns namtrace-all $namfile

# Define Finish procedure which is executed at the end of simulation
proc Finish {} {

    # Access global variables inside this procedure
    global ns ntrace namfile

    # Flush all remaining trace events to the files
    $ns flush-trace

    # Close the trace file and NAM file
    close $ntrace
    close $namfile

    # Launch the NAM animation
    exec nam lab11.nam &

    # Count and display the number of ping packets dropped
    puts "The number of ping packets dropped are:"
    exec grep "^d" lab11.tr | cut -d " " -f 5 | grep -c "ping" &

    # Exit the simulation
    exit 0
}

# Create six network nodes n(0) to n(5)
for {set i 0} {$i < 6} {incr i} {
    set n($i) [$ns node]
}

# Connect the nodes in a linear topology using duplex links
# Each link has 0.1 Mb bandwidth, 10 ms delay and DropTail queue
for {set j 0} {$j < 5} {incr j} {
    $ns duplex-link $n($j) $n([expr $j + 1]) 0.1Mb 10ms DropTail
}

# Define the receive function for Ping agent
# This function executes when a ping reply is received
Agent/Ping instproc recv {from rtt} {

    # Get the node to which the ping agent is attached
    $self instvar node_

    # Print the receiving node, sender node and RTT value
    puts "Node [$node_ id] received ping reply from $from with RTT = $rtt ms"
}

# Create Ping agent p0 and attach it to node n(0)
set p0 [new Agent/Ping]
$p0 set class_ 1
$ns attach-agent $n(0) $p0

# Create Ping agent p1 and attach it to node n(5)
set p1 [new Agent/Ping]
$p1 set class_ 1
$ns attach-agent $n(5) $p1

# Connect the two Ping agents
$ns connect $p0 $p1

# Set queue limit between node n(2) and n(3) to 2 packets
# This creates a bottleneck and causes packet drops during congestion
$ns queue-limit $n(2) $n(3) 2

# Position the queue in the middle of the link for NAM visualization
$ns duplex-link-op $n(2) $n(3) queuePos 0.5

# Create a TCP agent and attach it to node n(2)
# This agent generates congestion traffic
set tcp0 [new Agent/TCP]
$tcp0 set class_ 2
$ns attach-agent $n(2) $tcp0

# Create a TCP sink and attach it to node n(4)
set sink0 [new Agent/TCPSink]
$ns attach-agent $n(4) $sink0

# Connect the TCP agent and TCP sink
$ns connect $tcp0 $sink0

# Create a CBR application to generate continuous traffic
set cbr0 [new Application/Traffic/CBR]

# Set the CBR packet size to 500 bytes
$cbr0 set packetSize_ 500

# Set the CBR transmission rate to 1 Mb
$cbr0 set rate_ 1Mb

# Attach the CBR application to the TCP agent
$cbr0 attach-agent $tcp0

# Schedule ping packets before congestion
$ns at 0.2 "$p0 send"
$ns at 0.4 "$p1 send"

# Start TCP/CBR traffic to create congestion
$ns at 0.4 "$cbr0 start"

# Schedule ping packets during congestion (packet drops expected)
$ns at 0.8 "$p0 send"
$ns at 1.0 "$p1 send"

# Stop the congestion traffic
$ns at 1.2 "$cbr0 stop"

# Schedule ping packets after congestion
$ns at 1.4 "$p0 send"
$ns at 1.6 "$p1 send"

# End the simulation
$ns at 1.8 "Finish"

# Run the simulation
$ns run
