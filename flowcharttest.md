```mermaid
flowchart LR
  %% --- QueueingProcess ---
  Q0([QueueingProcess]) --> Q1[/Get Scan_execution list/]
  Q1 --> Q2{More items?}
  Q2 -- Yes --> Q3([Next item])
  Q3 --> Q4{Queue free && under limit?}
  Q4 -- No --> Q2
  Q4 -- Yes --> Q5[Enqueue]
  Q5 --> Q6[Insert into scan_execution_lock]
  Q6 --> Q7{Insert OK?}
  Q7 -- No --> Q8["Dequeue (rollback)"]
  Q8 --> Q2
  Q7 -- Yes --> Q2
  Q2 -- No --> Q9([Post-iteration])

  %% Flags consolidation (SAST then SCA)
  Q9 --> Q10[/Select by agent=machine, type=sast, del_at NULL/]
  Q10 --> Q11[Update Scan_execution.sast_in_queue = TRUE]
  Q11 --> Q12[/Select by agent=machine, type=sca, del_at NULL/]
  Q12 --> Q13[Update Scan_execution.sca_in_queue = TRUE]
  Q13 --> Q14([End])

  %% --- Worker (SastProcess / ScaProcess) loop ---
  W0([Worker Start]) --> W1[Dequeue]
  W1 --> W2{Item?}
  W2 -- No --> W3["[Wait & retry]"]
  W3 --> W1
  W2 -- Yes --> W4[Execute scan]
  W4 --> W5[Update scan_execution_lock.deleted_at]
  W5 --> W1
