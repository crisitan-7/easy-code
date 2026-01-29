flowchart TB
  subgraph QueueingProcess
    qStart([Start])
    fetch[/Get Scan_execution list/]
    hasNext{More items?}
    next([Next item])
    check{Queue free && under limit?}
    enq[Enqueue item]
    lockIns["Insert lock (scan_execution_lock)"]
    lockOk{Lock OK?}
    deq[Dequeue item]
    doneIter([Iteration done])
    selSast[/Select: agent=machineName, type=sast, deleted_at NULL/]
    updSast[Update se.sast_in_queue = TRUE]
    selSca[/Select: agent=machineName, type=sca, deleted_at NULL/]
    updSca[Update se.sca_in_queue = TRUE]
    qEnd([End])

    qStart --> fetch --> hasNext
    hasNext -- Yes --> next --> check
    check -- Yes --> enq --> lockIns --> lockOk
    lockOk -- Yes --> hasNext
    lockOk -- No --> deq --> hasNext
    check -- No --> hasNext
    hasNext -- No --> doneIter --> selSast --> updSast --> selSca --> updSca --> qEnd
  end

  subgraph Worker[SastProcess / ScaProcess]
    wStart([Start])
    wDeq[Dequeue from Queue]
    wHas{Item?}
    wWait["[Wait & retry]"]
    wExec[Execute scan]
    wClose[Update lock: set deleted_at]
    wEnd([End])

    wStart --> wDeq --> wHas
    wHas -- No --> wWait --> wDeq
    wHas -- Yes --> wExec --> wClose --> wDeq
  end
