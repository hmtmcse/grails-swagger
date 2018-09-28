URL Mapping
/



Controller
    Convention:
        a. Example : ApiNameV1Controller
        b. The URL will be api/v1/name/method

Action
    Convention:
        a. Example : getList, postCreate, deleteDelete
        b. The URL will be api/v1/name/list (only Allow get Request)
        c. The URL will be api/v1/name/create (only Allow post Request with json data)

        
List and get data
    params
        1. offset
        2. max
        3. sort: column name
        4. order: asc / desc according that column
        5. select:[name, id, x, y, z]
        6. where
               equal: (key: column, value: value)
               notEqual: (key: column, value: value)
               lessThan: (key: column, value: value)
               lessThanEqual: (key: column, value: value)
               getterThan: (key: column, value: value)
               getterThanEqual: (key: column, value: value)
               inList: (key: column, value: [value, value, value])
               and: { all above condition with and format}
               or: { all above condition with or format}


Request Processing Mechanism
    Steps
        1. Check the Allowed http method
        2. Check the content type
        3. Validate or Refine the parameter
        4. Process the parameter
        5. Process Conditions                