package com.example.jwtauth.kotestExample

import io.kotest.core.spec.style.*
import io.kotest.matchers.shouldBe
import java.math.BigInteger

class MyTests: FunSpec( {
    test("새로운 테스트를 실행") {
        1 + 3 shouldBe 4
        "kotlin".length shouldBe 6
    }
})

class AnotherTest: StringSpec({
    "킷캣은 맛있다." {
        "킷캣은 맛있다.".contains("킷캣") shouldBe true
    }
})

class ShouldTest: ShouldSpec({
    context("context should 테스트") {
        should("57은 소수이다.") {
            BigInteger("57").isProbablePrime(100) shouldBe false
        }
        xshould("이 테스트는 건너 뛴다.") {
            "hello".length shouldBe 5
        }
    }
})

class DescribeTest: DescribeSpec({
    describe("사칙연산을 해보자") {
        it("2 * 2는 4이다.") {
            2 * 2 shouldBe 4
        }

        it("92 / 4는 23이다.") {
            92 / 4 shouldBe 23
        }
    }
})

class BehaviorTest: BehaviorSpec({
    given("int형 가변 변수 a와 b가 있다.") {
        var a: Int
        var b: Int
        `when`("a에는 8 b에는 7이 주어진다.") {
            a = 8
            b = 7
            then("a와 b를 더하면 15가 나온다.") {
                a + b shouldBe 15
            }
        }
    }
})