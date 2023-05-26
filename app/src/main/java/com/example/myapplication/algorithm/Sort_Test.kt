package com.example.myapplication.algorithm

class Sort_Test {

    var arr = intArrayOf(1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 9)

    fun sort(num: Int): Int {
        var arr = intArrayOf(1, 3, 4, 5, 5, 5, 5, 5, 9)
        var start = 0
        var end = arr.size - 1

        var prevStart = start
        while (start <= end) {
            // 防止 (end + start) / 2 数值移除
            var mid = start + (end - start) / 2
            if (arr[mid] === num) {
                return mid
            } else if (arr[mid] > num) {
                end = mid - 1
            } else if (arr[mid] < num) {
                start = mid + 1
            }
        }
        return -1
    }

    fun findFirst(num: Int): Int {
        var start = 0
        var end = arr.size - 1
        while (start <= end) {
            // 防止 (end + start) / 2 数值溢出
            var mid = start + (end - start) / 2
            if (arr[mid] === num) {
                var i = findFirst(start, mid - 1, num)
                if (i >= 0) {
                    return i
                }
                return mid
            } else if (arr[mid] > num) {
                end = mid - 1
            } else if (arr[mid] < num) {
                start = mid + 1
            }
        }
        return -1
    }

    fun findFirst(start0: Int, end0: Int, num: Int): Int {
        var start = start0
        var end = end0
        while (start <= end) {
            // 防止 (end + start) / 2 数值移除
            var mid = start + (end - start) / 2
            if (arr[mid] === num) {
                var i = findFirst(start, mid - 1, num)
                if (i >= 0) {
                    return i
                }
                return mid
            } else if (arr[mid] > num) {
                end = mid - 1
            } else if (arr[mid] < num) {
                start = mid + 1
            }
        }
        return -1
    }
}