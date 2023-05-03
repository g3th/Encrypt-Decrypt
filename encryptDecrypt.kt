package encryptdecrypt
import java.io.*
import java.util.*
import kotlin.system.exitProcess

fun fileCreation(file: String) {
    File(file).writeText("")
}

fun getFile(file: String): List<String>{
    return File(file).readLines()
}

fun writeFile(directory: String, content: String){
    File(directory).appendText(content)
}

fun encrypt(string: String, key: Int, algo: String): String{
    var result = ""
    var alphabet = ('a'..'z').toList()
    when {
        algo == "unicode" -> {
            string.forEach {
                result += it + key
            }
        }
        algo == "shift" -> {
            for (i in string) {
                if (i == ' ' || !i.isLetter()) {
                    result += i
                }
                for (j in alphabet.indices) {
                    if (i == alphabet[j]) {
                        Collections.rotate(alphabet, -key)
                        result += alphabet[j]
                        Collections.rotate(alphabet, +key)
                    } else if (i.uppercase() == alphabet[j].uppercase()) {
                        Collections.rotate(alphabet, -key)
                        result += alphabet[j].uppercase()
                        Collections.rotate(alphabet, +key)
                    }
                }
            }
        }
    }
    return result
}

fun decrypt(string: String, key: Int, algo: String): String{
    var result = ""
    var alphabet = ('a'..'z').toList()
    when {
        algo == "unicode" -> {
            string.forEach {
                result += it - key
            }
        }
        algo == "shift" -> {
            for (i in string) {
                if (i == ' ' || !i.isLetter()) {
                    result += i
                }
                for (j in alphabet.indices) {
                    if (i == alphabet[j]) {
                        Collections.rotate(alphabet, +key)
                        result += alphabet[j]
                        Collections.rotate(alphabet, -key)
                    } else if (i.uppercase() == alphabet[j].uppercase()) {
                        Collections.rotate(alphabet, +key)
                        result += alphabet[j].uppercase()
                        Collections.rotate(alphabet, -key)
                    }
                }
            }
        }
    }
    return result
}

fun main(args: Array<String>) {
    var mode = "enc"
    var key = "0"
    var data = ""
    var alg = "shift"
    var out = ""
    var fileName = ""
    val storedModeResult = mutableListOf<String>()

    for (i in args.indices) {
        when {
            args[i] == "-mode" ->  mode = args[i + 1]
            args[i] == "-data" -> data = args[i + 1]
            args[i] == "-key" -> key = args[i + 1]
            args[i] == "-alg" -> alg = args[i + 1]
            args[i] == "-out" -> out = args[i + 1]
            args[i] == "-in" -> fileName = args[i + 1]
            args[i] == "-in" && args[i].contains("data") -> fileName = ""
        }
    }
        try {
            when {
                fileName.isNotEmpty() -> {
                    for (j in getFile(fileName)) {
                        if (mode == "enc") {
                            storedModeResult.add(encrypt(j, key.toInt(), alg))
                        } else if (mode == "dec") {
                            storedModeResult.add(decrypt(j, key.toInt(), alg))
                        }
                    }
                }

                data.isNotEmpty() || data.isNotEmpty() && fileName.isNotEmpty() -> {
                    if (mode == "enc") {
                        storedModeResult.add(encrypt(data, key.toInt(), alg))
                    } else if (mode == "dec") {
                        storedModeResult.add(decrypt(data, key.toInt(), alg))
                    }
                }
            }
        } catch(e: Exception){
            println("Error")
            exitProcess(1)
        }

    when {
        out.isNotEmpty() -> {
            fileCreation(out)
            for (i in storedModeResult.indices) {
                if (i < storedModeResult.size -1) {
                    writeFile(out, storedModeResult[i])
                    writeFile(out, "\n")
                } else {
                    writeFile(out, storedModeResult[i])
                }
            }
        }

        out.isEmpty() -> {
            println(storedModeResult[0])
        }
    }
}
