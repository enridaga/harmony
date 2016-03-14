
;; this is equivalent to test7 in BlocksworldTest
(define (problem blocksworld)
    (:domain blocksworld)
    (:objects A B C D E F G H I J K L M - Block)
    (:init
        (ArmEmpty)
        (OnTable J)
        (On K J)
        (On L K)
        (On M L)
        (Clear M)
        
        (OnTable E)
        (On F E)
        (On G F)
        (On H G)
        (On I H)
        (Clear I)
        
        (OnTable A)
        (On B A)
        (On C B)
        (On D C)
        (Clear D)
        
    )
    (:goal
        (and
            (OnTable A)
            (On B A)
            (On C B)
            (On D C)
            (On G D)
            (Clear G)
        )
    )
)