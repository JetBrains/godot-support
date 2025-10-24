enum Constant {
    PASS1 = 0,
    PASSED = PASS1+1, # enum element can reference other enum element
    CANNOT = OK, # can not reference before it was declared
    OK = 0,
}