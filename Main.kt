package sorting

import java.io.File
import java.util.*

fun sortInt(natural: Boolean, type: String, sep: String, inFile: File?, outFile: File?, process: (String) -> List<String>) {
    val rawData: List<String>
    if (inFile != null) {
        rawData = inFile.readLines().map { process(it) }.flatten()
    } else {
        val sc = Scanner(System.`in`)
        sc.useDelimiter("\n")
        rawData = sc.tokens().toList().map { process(it) }.flatten()
    }
    val data = emptyList<Int>().toMutableList()
    for (item in rawData) {
        if (Regex("[+-]?\\d+").matches(item)) {
            data.add(item.toInt())
        } else {
            println("\"$item\" is not a long. It will be skipped.")
        }
    }
    var output = "Total $type: ${data.size}."
    if (natural) {
        output += "\nSorted data:$sep${data.sorted().joinToString(sep)}"
    } else {
        val countMap = emptyMap<Int, Int>().toMutableMap()
        for (d in data.toSet()) {
            countMap[d] = data.count { it == d }
        }
        for (item in countMap.toList().sortedBy { it.first }.sortedBy { it.second }) {
            output += "\n${item.first}: ${item.second} time(s), ${100 * item.second / data.size}%"
        }
    }
    if (outFile != null) {
        outFile.writeText(output)
    } else {
        println(output)
    }
}

fun sortStr(natural: Boolean, type: String, sep: String, inFile: File?, outFile: File?, process: (String) -> List<String>) {
    val data: List<String>
    if (inFile != null) {
        data = inFile.readLines().map { process(it) }.flatten()
    } else {
        val sc = Scanner(System.`in`)
        sc.useDelimiter("\n")
        data = sc.tokens().toList().map { process(it) }.flatten()
    }
    var output = "Total $type: ${data.size}."
    if (natural) {
        output += "\nSorted data:$sep${data.sorted().joinToString(sep)}"
    } else {
        val countMap = emptyMap<String, Int>().toMutableMap()
        for (d in data.toSet()) {
            countMap[d] = data.count { it == d }
        }
        for (item in countMap.toList().sortedBy { it.first }.sortedBy { it.second }) {
            output += "\n${item.first}: ${item.second} time(s), ${100 * item.second / data.size}%"
        }
    }
    if (outFile != null) {
        outFile.writeText(output)
    } else {
        println(output)
    }
}

fun main(args: Array<String>) {
    if (args.contains("-sortingType") && (args.size < args.indexOf("-sortingType") + 2 || args[args.indexOf("-sortingType") + 1] !in listOf("natural", "byCount"))) {
        println("No sorting type defined!")
        return
    }
    if (args.contains("-dataType") && (args.size < args.indexOf("-dataType") + 2 || args[args.indexOf("-dataType") + 1] !in listOf("long", "line", "word"))) {
        println("No data type defined!")
        return
    }
    if (args.contains("-inputFile") && (args.size < args.indexOf("-inputFile") + 2 || !File(args[args.indexOf("-inputFile") + 1]).exists())) {
        println("Input file is missing")
        return
    }
    if (args.contains("-outputFile") && args.size < args.indexOf("-inputFile") + 2) {
        println("Output file is missing")
        return
    }
    for (arg in args) {
        if (arg[0] == '-' && arg !in listOf("-sortingType", "-dataType", "-inputFile", "-outputFile")) {
            println("\"$arg\" is not a valid parameter. It will be skipped.")
        }
    }
    val inFile = if (args.contains("-inputFile")) File(args[args.indexOf("-inputFile") + 1]) else null
    val outFile = if (args.contains("-outputFile")) File(args[args.indexOf("-outputFile") + 1]) else null
    val natural = if (args.contains("-sortingType")) args[args.indexOf("-sortingType") + 1] == "natural" else true
    when (if (args.contains("-dataType")) args[args.indexOf("-dataType") + 1] else "long") {
        "long" -> sortInt(natural, "numbers", " ", inFile, outFile) { d: String -> d.split(Regex("\\s+")).dropLastWhile { it == "" } }
        "line" -> sortStr(natural, "lines", "\n", inFile, outFile) { d: String -> d.split("\n") }
        "word" -> sortStr(natural, "words", " ", inFile, outFile) { d: String -> d.split(Regex("\\s+")).dropLastWhile { it == "" } }
    }
}