package channel.multiplexing2

import channel.*
import channel.boring.boring

// https://talks.golang.org/2012/concurrency.slide#27

suspend fun fanIn(input1: ReceiveChannel<String>, input2: ReceiveChannel<String>): ReceiveChannel<String> = suspending {
    val c = Channel<String>()
    go {
        while(true) {
            val s = select<String> {
                input1.onReceive { it }
                input2.onReceive { it }
            }
            c.send(s)
        }
    }
    c // return combo channel
}

fun main(args: Array<String>) = go.main {
    val c = fanIn(boring("Joe"), boring("Ann"))
    for (i in 0..9) {
        println(c.receive())
    }
    println("Your're both boring; I'm leaving.")
}