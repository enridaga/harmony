

;; Blocksworld domain
(define (domain blocksworld)
    (:requirements :strips)
    (:types
        Block
    )
    (:predicates
        (On ?x - Block ?y - Block)
        (ArmEmpty)
        (Holding ?x - Block)
        (OnTable ?x - Block)
        (Clear ?x - Block)
    )
    
    (:action PickUp 
       :parameters (?x - Block)
       :precondition (and (OnTable ?x) (Clear ?x) (ArmEmpty) )
       :effect (and (Holding ?x) (not (ArmEmpty)) (not (OnTable ?x)) ) 
    )    
    (:action PutDown
       :parameters (?x - Block)
       :precondition (and (Holding ?x) ) ;; (not (ArmEmpty)) 
       :effect (and (Clear ?x) (ArmEmpty) (OnTable ?x) (not (Holding ?x)) )
    )
    (:action Stack
       :parameters (?x ?y - Block)
       :precondition (and (Clear ?y) (Holding ?x) )
       :effect (and (On ?x ?y) (Clear ?x) (ArmEmpty) (not (Clear ?y)) (not (Holding ?x)) )
    )
    (:action Unstack
       :parameters (?x ?y - Block)
       :precondition (and (On ?x ?y) (ArmEmpty) (Clear ?x) )
       :effect (and (Holding ?x) (Clear ?y) (not (On ?x ?y)) (not (ArmEmpty)) )
    )   
)