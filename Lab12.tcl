# ==============================
# Create the Simulator Object
# ==============================
# This object controls the entire NS-2 simulation
set ns [new Simulator]

# ==============================
# Define colors for traffic flows
# ==============================
# Flow ID 1 will be shown in Blue
# Flow ID 2 will be shown in Red
$ns color 1 Blue
$ns color 2 Red

# ==============================
# Open trace file to record events
# ==============================
# lab12.tr stores packet-level simulation details
set ntrace [open lab12.tr w]
$ns trace-all $ntrace

# ==============================
# Open NAM trace file
# ==============================
# lab12.nam is used for graphical animation in NAM
set namfile [open lab12.nam w]
$ns namtrace-all $namfile

# ==============================
# Files to store congestion window values
# ==============================
# These files are used by xgraph to plot cwnd vs time
set winFile0 [open WinFile0 w]
set winFile1 [open WinFile1 w]

# ==============================
# Finish Procedure
# ==============================
# This procedure runs at the end of the simulation
proc Finish {} {

    # Access global variables
    global ns ntrace namfile

    # Flush remaining trace data
    $ns flush-trace

    # Close trace files
    close $ntrace
    close $namfile

    # Launch NAM animation
    exec nam lab12.nam &

    # Plot congestion window graphs using xgraph
    exec xgraph WinFile0 WinFile1 &

    # Exit the simulation
    exit 0
}

# ==============================
# Procedure to plot congestion window
# ==============================
# tcpSource -> TCP agent
# file      -> output file for cwnd values
proc PlotWindow {tcpSource file} {

    global ns
    # Time interval for recording cwnd
    set time 0.1
    # Get current simulation time
    set now [$ns now]
    # Get current congestion window size
    set cwnd [$tcpSource set cwnd_]
    # Write time and cwnd value to file
    puts $file "$now $cwnd"
    # Schedule next cwnd sampling
    $ns at [expr $now+$time] "PlotWindow $tcpSource $file"
}

# ==============================
# Create 6 network nodes
# ==============================
for {set i 0} {$i<6} {incr i} {
    # Create node n(0) to n(5)
    set n($i) [$ns node]
}

# ==============================
# Create duplex links between nodes
# ==============================
# High bandwidth links from n(0) and n(1) to n(2)
$ns duplex-link $n(0) $n(2) 2Mb 10ms DropTail
$ns duplex-link $n(1) $n(2) 2Mb 10ms DropTail

# Bottleneck link with low bandwidth and high delay
$ns duplex-link $n(2) $n(3) 0.6Mb 100ms DropTail

# ==============================
# Create an Ethernet LAN
# ==============================
# Nodes n(3), n(4), and n(5) form a LAN
# Uses Ethernet MAC (802.3)
set lan [$ns newLan "$n(3) $n(4) $n(5)" 0.5Mb 40ms LL Queue/DropTail MAC/802_3 Channel]

# ==============================
# Node orientation for NAM display
# ==============================
# These settings affect only visualization
$ns duplex-link-op $n(0) $n(2) orient right-down
$ns duplex-link-op $n(1) $n(2) orient right-up
$ns duplex-link-op $n(2) $n(3) orient right

# ==============================
# Queue configuration on bottleneck link
# ==============================
# Limit queue size to 20 packets
$ns queue-limit $n(2) $n(3) 20

# Display queue position in NAM
$ns duplex-link-op $n(2) $n(3) queuePos 0.5

# ==============================
# Error model for packet loss
# ==============================
# Introduces random packet drops
set loss_module [new ErrorModel]
$loss_module ranvar [new RandomVariable/Uniform]
$loss_module drop-target [new Agent/Null]
$ns lossmodel $loss_module $n(2) $n(3)

# ==============================
# TCP Connection 1: n(0) → n(4)
# ==============================
# Create TCP NewReno agent
set tcp0 [new Agent/TCP/Newreno]

# Assign flow ID
$tcp0 set fid_ 1

# Set TCP window size and packet size
$tcp0 set window_ 8000
$tcp0 set packetSize_ 552

# Attach TCP agent to source node n(0)
$ns attach-agent $n(0) $tcp0

# Create TCP sink with delayed ACK
set sink0 [new Agent/TCPSink/DelAck]

# Attach sink to destination node n(4)
$ns attach-agent $n(4) $sink0

# Connect TCP source and sink
$ns connect $tcp0 $sink0

# ==============================
# FTP application over TCP (Flow 1)
# ==============================
set ftp0 [new Application/FTP]
$ftp0 attach-agent $tcp0
$ftp0 set type_ FTP

# ==============================
# TCP Connection 2: n(5) → n(1)
# ==============================
set tcp1 [new Agent/TCP/Newreno]
$tcp1 set fid_ 2
$tcp1 set window_ 8000
$tcp1 set packetSize_ 552

# Attach TCP agent to source node n(5)
$ns attach-agent $n(5) $tcp1

# Create TCP sink with delayed ACK
set sink1 [new Agent/TCPSink/DelAck]

# Attach sink to destination node n(1)
$ns attach-agent $n(1) $sink1

# Connect TCP source and sink
$ns connect $tcp1 $sink1

# ==============================
# FTP application over TCP (Flow 2)
# ==============================
set ftp1 [new Application/FTP]
$ftp1 attach-agent $tcp1
$ftp1 set type_ FTP

# ==============================
# Schedule simulation events
# ==============================
# Start first FTP and cwnd plotting
$ns at 0.1 "$ftp0 start"
$ns at 0.1 "PlotWindow $tcp0 $winFile0"

# Start second FTP and cwnd plotting later
$ns at 0.5 "$ftp1 start"
$ns at 0.5 "PlotWindow $tcp1 $winFile1"

# Stop traffic
$ns at 25.0 "$ftp0 stop"
$ns at 25.1 "$ftp1 stop"

# Call Finish procedure
$ns at 25.2 "Finish"
# ==============================
# Run the simulation
# ==============================
$ns run
