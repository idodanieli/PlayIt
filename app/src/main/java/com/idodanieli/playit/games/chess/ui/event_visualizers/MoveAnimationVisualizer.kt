import android.graphics.RectF

fun RectF.add(other: RectF): RectF {
    return RectF(
        left + other.left,
        top + other.top,
        right + other.right,
        bottom + other.bottom
    )
}

fun RectF.subtract(other: RectF): RectF {
    return RectF(
        left - other.left,
        top - other.top,
        right - other.right,
        bottom - other.bottom
    )
}

fun RectF.multiply(amount: Float): RectF {
    return RectF(
        left * amount,
        top * amount,
        right * amount,
        bottom * amount
    )
}