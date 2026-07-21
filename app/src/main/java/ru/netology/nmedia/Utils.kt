package ru.netology.nmedia

fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 10_000 -> {
            val thousands = count / 1000
            val hundreds = (count % 1000) / 100
            if (hundreds == 0) "${thousands}K"
            else "${thousands}.${hundreds}K"
        }

        count < 1_000_000 -> {
            val thousands = count / 1000
            if (thousands < 10) "${thousands}K"
            else "${thousands}K"
        }

        count < 1_000_000_000 -> {
            val millions = count / 1_000_000
            val hundredsThousands = (count % 1_000_000) / 100_000
            if (hundredsThousands == 0) "${millions}M"
            else "${millions}.${hundredsThousands}M"
        }

        else -> "${count / 1_000_000_000}B"
    }
}