=MID(D2, SEARCH("画面定義書_", D2) + LEN("画面定義書_"), SEARCH("_", D2, SEARCH("画面定義書_", D2) + LEN("画面定義書_")) - (SEARCH("画面定義書_", D2) + LEN("画面定義書_")))
=IF(ISNUMBER(SEARCH(K2, B2)), TRUE, FALSE)
