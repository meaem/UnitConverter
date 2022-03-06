package converter

class Unit(
//    val rate: (Double) -> Double,
    val name: String,
    val shortName: String,
    val pluralName: String,
    val group: String,
    val extraNames: List<String> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Unit

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class Converter(
    val unit1: Unit, val unit2: Unit,
    private val unit1ToUnit2: (Double) -> Double,
    private val unit2ToUnit1: (Double) -> Double
) {

    constructor(unit1: Unit, unit2: Unit, rate: Double) : this(
        unit1,
        unit2,
        { x: Double -> x * rate },
        { x: Double -> x / rate })

    fun canConvert(x: Unit, y: Unit): Boolean {

        val l1 = listOf(x, y)
//        val l2  = listOf(y,x)

        val l3 = listOf(unit1, unit2)
        val l4 = listOf(unit2, unit1)
        val l5 = listOf(unit1, unit1)
        val l6 = listOf(unit2, unit2)



        return l1 in listOf(l3, l4, l5, l6)
    }

    fun canConvertFrom(x: Unit): Boolean {
        return x == unit1 || x == unit2
    }

    fun canConvertTo(x: Unit): Boolean {
        return x == unit1 || x == unit2
    }

    fun convert(amount: Double, fromUnit: Unit, toUnit: Unit): Double {
        require(canConvert(fromUnit, toUnit))
        if (fromUnit == toUnit)
            return amount
        else {
            if (fromUnit == unit1 && toUnit == unit2) {
                return unit1ToUnit2(amount)
            } else // (fromUnit == unit2 && toUnit == unit1)
            {
                return unit2ToUnit1(amount)
            }
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Converter

        if (unit1 == other.unit1 && unit2 == other.unit2) return true
        if (unit1 == other.unit2 && unit2 == other.unit1) return true

        return false
    }

    override fun hashCode(): Int {
        var result = 31 * unit1.hashCode()
        result += 31 * unit2.hashCode()
        return result
    }


}

//
//fun meterTometer(value: Double) = value * 1.0
//fun kilometerTometer(value: Double) = value * 1000.0
//fun centimeterTometer(value: Double) = value * 0.01
//fun millimeterTometer(value: Double) = value * 0.001

//fun mileTometer(value: Double) = value * 1609.35
//fun yardTometer(value: Double) = value * 0.9144
//fun footTometer(value: Double) = value * 0.3048
//fun inchTometer(value: Double) = value * 0.0254
//
//fun gramTogram(value: Double) = value * 1.0
//fun kilogramTogram(value: Double) = value * 1000.0
//fun milligramTogram(value: Double) = value * 0.001
//fun poundTogram(value: Double) = value * 453.592
//fun ounceTogram(value: Double) = value * 28.3495

fun celsiusToFahrenheit(value: Double) = value * 9.0 / 5 + 32
fun fahrenheitToCelsius(value: Double) = (value - 32) * 5.0 / 9

fun fahrenheitToKelvin(value: Double) = (value + 459.67) * 5.0 / 9
fun kelvinToFahrenheit(value: Double) = value * 9.0 / 5 - 459.67

fun kelvinToCelsius(value: Double) = value - 273.15
fun celsiusToKelvin(value: Double) = value + 273.15

fun other(value: Double) = value * 0.0

val otherUnit = Unit("???", "o", "???", "")

val meter = Unit("meter", "m", "meters", "d")
val kilometer = Unit("kilometer", "km", "kilometers", "d")
val centimeter = Unit("centimeter", "cm", "centimeters", "d")
val millimeter = Unit("millimeter", "mm", "millimeters", "d")
val mile = Unit("mile", "mi", "miles", "d")
val yard = Unit("yard", "yd", "yards", "d")
val foot = Unit("foot", "ft", "feet", "d")
val inch = Unit("inch", "in", "inches", "d")
val gram = Unit("gram", "g", "grams", "w")
val kilogram = Unit("kilogram", "kg", "kilograms", "w")
val milligram = Unit("milligram", "mg", "milligrams", "w")
val pound = Unit("pound", "lb", "pounds", "w")
val ounce = Unit("ounce", "oz", "ounces", "w")
val celsius = Unit("degree Celsius", "dc", "degrees Celsius", "t", listOf("celsius", "c"))
val fahrenheit = Unit("degree Fahrenheit", "df", "degrees Fahrenheit", "t", listOf("fahrenheit", "f"))
val kelvin = Unit("kelvin", "k", "kelvins", "t")

val units = listOf(
    meter, kilometer, millimeter, centimeter, mile, yard, foot, inch,
    gram, kilogram, milligram, pound, ounce,
    celsius, fahrenheit, kelvin
)
val converters = listOf(
//    Converter(meter, meter, 1.0),
    Converter(kilometer, meter, 1000.0),
    Converter(meter, centimeter, 100.0),
    Converter(meter, millimeter, 1000.0),
    Converter(mile, meter, 1609.35),
    Converter(yard, meter, 0.9144),
    Converter(foot, meter, 0.3048),
    Converter(inch, meter, 0.0254),

//    Converter(gram, gram, 1.0),
    Converter(kilogram, gram, 1000.0),
    Converter(gram, milligram, 1000.0),
    Converter(pound, gram, 453.592),
    Converter(ounce, gram, 28.3495),

    Converter(celsius, fahrenheit, ::celsiusToFahrenheit, ::fahrenheitToCelsius),
    Converter(celsius, kelvin, ::celsiusToKelvin, ::kelvinToCelsius),
    Converter(fahrenheit, kelvin, ::fahrenheitToKelvin, ::kelvinToFahrenheit),


    )


fun byUnitName(unitname: String): Unit {
    for (v in units) {
        if (unitname.lowercase() in mutableListOf(
                v.name,
                v.shortName,
                v.pluralName,
            ).also { it.addAll(v.extraNames) }.map { it.lowercase() }
        )
            return v
    }
    return otherUnit
}


fun main() {
//    val meter = Unit("meter", "m", "meters", "d")
//    val meter2 = Unit("meter", "m", "meters", "d")
//    val kilometer = Unit("kilometer", "km", "kilometers", "d")
//    val l1 = listOf(meter,kilometer)
//    val l2 = listOf(kilometer,meter)
//    val l3 = listOf(kilometer,meter2)

//    println("${meter.name} ${kilometer.name} ${meter == kilometer}")
//    println("${l1} ${l2} ${l1 == l2}")
//    println("${l2} ${l3} ${l2 == l3}")
    val pattern =
        "\\s*(?<val>[-+]?\\d+(\\.\\d+)?)\\s+(?<sunit>(degrees?\\s+)?\\w+)\\s+(?<dir>\\w+)\\s+(?<dunit>(degrees?\\s+)?\\w+)\\s*".toRegex()
    print("Enter what you want to convert (or exit): ")
    var input = readln().lowercase()
    while (input != "exit") {
        val match = pattern.find(input)

        if (match != null) {

//            val input = input.trim().replace("\\s+".toRegex(), " ")
//            val tokens = input.split(" ")
            val sn = match.groups["val"]?.value!!.trim()
            val u_src = match.groups["sunit"]?.value!!.trim()
            val direction = match.groups["dir"]?.value!!.trim()
            val u_dist = match.groups["dunit"]?.value!!.trim()
            val src_num = sn.toDouble()
//            File("log.txt").appendText("$input\n")
//            File("log.txt").appendText("source value: $src_num , source unit: $u_src, direction: $direction, dist unit: $u_dist\n")

            val unit_src = byUnitName(u_src)
            val unit_dist = byUnitName(u_dist)
            convert(src_num, unit_src, unit_dist)

            println("")
        } else {
            println("Parse error")
        }
        println("Enter what you want to convert (or exit): ")
        input = readln().lowercase()
    }
}

fun convert(src_num: Double, unit_src: Unit, unit_dist: Unit) {
    if (src_num < 0 && (unit_src.group == "d")) {
        println("Length shouldn't be negative.")
        return
    }
    if (src_num < 0 && (unit_dist.group == "w")) {
        println("Weight shouldn't be negative.")
        return
    }

    val converter = converters.filter { it.canConvert(unit_src, unit_dist) }
    var result = 0.0
    if (converter.isNotEmpty()) {
        result = converter[0].convert(src_num, unit_src, unit_dist)

        val fromunit = if (src_num != 1.0) unit_src.pluralName else unit_src.name
        val tounit = if (result != 1.0) unit_dist.pluralName else unit_dist.name

//        val toUnit = if (result == 1.0) "meter" else "meters"
        println("${src_num} $fromunit is $result $tounit")

    } else {    //if (converter.size > 1)
        val c1 = converters.filter { it.canConvertFrom(unit_src) }
        val c2 = converters.filter { it.canConvertTo(unit_dist) }
        var firstConverter: Converter? = null
        var secondConverter: Converter? = null

        outer@ for (x in c1) {
            val u = if (x.unit1 == unit_src) x.unit2 else x.unit1
            for (y in c2) {
                if (y.canConvert(u, unit_dist)) {
                    firstConverter = x
                    secondConverter = y
                    break@outer
                }
            }
        }
        if (firstConverter != null && secondConverter != null) {
            val hup = setOf(firstConverter.unit1, firstConverter.unit2).intersect(
                setOf(
                    secondConverter.unit1,
                    secondConverter.unit2
                )
            )!!.first()
            result = firstConverter.convert(src_num, unit_src, hup)
            result = secondConverter.convert(result, hup, unit_dist)
            val fromunit = if (src_num != 1.0) unit_src.pluralName else unit_src.name
            val tounit = if (result != 1.0) unit_dist.pluralName else unit_dist.name

//        val toUnit = if (result == 1.0) "meter" else "meters"
            println("${src_num} $fromunit is $result $tounit")
        } else {    //    if (converter.isEmpty()){
//    if (unit_src.group != unit_dist.group) {
//                File("log.txt").appendText("Conversion from ${unit_src.pluralName} to ${unit_dist.pluralName} is impossible\n")
            println("Conversion from ${unit_src.pluralName} to ${unit_dist.pluralName} is impossible")

        }

    }


}
