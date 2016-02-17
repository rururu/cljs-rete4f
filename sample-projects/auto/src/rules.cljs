(ns rules)

(def RULES '(
  (normal-engine-state-conclusions
   8
   (working-state engine normal)
   =>
   (asser repair advice "No repair needed.")
   (asser spark-state engine normal)
   (asser charge-state battery charged)
   (asser rotation-state engine rotates))

  (unsatisfactory-engine-state-conclusions
   8
   (working-state engine unsatisfactory)
   =>
   (asser charge-state battery charged)
   (asser rotation-state engine rotates))

  (determine-engine-state-0
   8
   (stage value diagnose)
   (not working-state engine ?ws)
   (not repair advice ?a)
   =>
   (f/ask-yorno "Does the engine start?" 'ENGINE-STARTS))

  (determine-engine-state-1
    8
    ?q (asked question ENGINE-STARTS)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (case ?a
      "yes" (f/ask-yorno "Does the engine run normally?" 'RUN-NORM)
      "no" (asser working-state engine does-not-start)))

  (determine-engine-state-2
    8
    ?q (asked question RUN-NORM)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (case ?a
      "yes" (asser working-state engine normal)
      "no" (asser working-state engine unsatisfactory)))

  (determine-rotation-state-0
    0
    (working-state engine does-not-start)
    (not rotation-state engine ?rs)
    (not repair advice ?a)
    =>
    (f/ask-yorno "Does the engine rotate?" 'ENGINE-ROTATE))

  (determine-rotation-state-1
    0
    ?q (asked question ENGINE-ROTATE)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (case ?a
      "yes" (do
              (asser rotation-state engine rotates)
              (asser spark-state engine irregular-spark))
      "no"  (do
              (asser rotation-state engine does-not-rotate)
              (asser spark-state engine does-not-spark))))

  (determine-sluggishness-0
    3
    (working-state engine unsatisfactory)
    (not repair advice ?a)
    =>
    (f/ask-yorno "Is the engine sluggish?" 'ENGINE-SLUG))

  (determine-sluggishness-1
    3
    ?q (asked question ENGINE-SLUG)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (if (= ?a "yes")
     (asser repair advice "Clean the fuel line.")))

  (determine-misfiring-0
    2
    (working-state engine unsatisfactory)
    (not repair advice ?a)
    (not asked question ?q)
    (not have checked ENGINE-MISFIRE)
    =>
    (f/ask-yorno "Does the engine misfire?" 'ENGINE-MISFIRE))

  (determine-misfiring-1
    2
    ?q (asked question ENGINE-MISFIRE)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (when (= ?a "yes")
      (asser repair advice "Point gap adjustment.")
      (asser spark-state engine irregular-spark))
    (asser have checked ENGINE-MISFIRE))

  (determine-knocking-0
    1
    (working-state engine unsatisfactory)
    (not repair advice ?a)
    (not asked question ?q)
    (not have checked ENGINE-KNOCK)
    =>
    (f/ask-yorno "Does the engine knock?" 'ENGINE-KNOCK))

  (determine-knocking-1
    1
    ?q (asked question ENGINE-KNOCK)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (if (= ?a "yes")
      (asser repair advice "Timing adjustment."))
    (asser have checked ENGINE-KNOCK))

  (determine-low-output-0
    0
    (working-state engine unsatisfactory)
    (not symptom engine ?se)
    (not repair advice ?a)
    (not asked question ?q)
    =>
    (f/ask-yorno "Is the output of the engine low?" 'OUTPUT-LOW))

  (determine-low-output-1
    0
    ?q (asked question OUTPUT-LOW)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (case ?a
      "yes" (asser symptom engine low-output)
      "no" (asser symptom engine not-low-output)))

  (determine-gas-level-0
    0
    (working-state engine does-not-start)
    (rotation-state engine rotates)
    (not repair advice ?a)
    (not asked question ?q)
    (not have checked GAS-LEVEL)
    =>
    (f/ask-yorno "Does the tank have any gas in it?" 'GAS-LEVEL))

  (determine-gas-level-1
    0
    ?q (asked question GAS-LEVEL)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (if (= ?a "no")
      (asser repair advice "Add gas."))
    (asser have checked GAS-LEVEL))

  (determine-battery-state-0
    0
    (rotation-state engine does-not-rotate)
    (not charge-state battery ?cs)
    (not repair advice ?a)
    =>
    (f/ask-yorno "Is the battery charged?" 'BATT-CHARG))

  (determine-battery-state-1
    0
    ?q (asked question BATT-CHARG)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (case ?a
      "yes" (asser charge-state battery charged)
      "no" (do
             (asser repair advice "Charge the battery.")
             (asser charge-state battery dead))))

  (determine-point-surface-state-10
    8
    (working-state engine does-not-start)
    (spark-state engine irregular-spark)
    (not repair advice ?a)
    =>
    (f/ask "What is the surface state of the points?"
           ["normal" "burned" "contaminated"]
           'STAT-POINTS))

  (determine-point-surface-state-11
    8
    ?q (asked question STAT-POINTS)
    ?a (received answer "normal")
    =>
    (retract ?q ?a))

  (determine-point-surface-state-12
    8
    ?q (asked question STAT-POINTS)
    ?a (received answer "burned")
    =>
    (retract ?q ?a)
    (asser repair advice "Replace the points."))

  (determine-point-surface-state-13
    8
    ?q (asked question STAT-POINTS)
    ?a (received answer "contaminated")
    =>
    (retract ?q ?a)
    (asser repair advice "Clean the points."))

  (determine-point-surface-state-2
    0
    (symptom engine low-output)
    (not repair advice ?a)
    =>
    (f/ask "What is the surface state of the points?"
           ["normal" "burned" "contaminated"]
           'STAT-POINTS))

  (determine-conductivity-test-0
    0
    (working-state engine does-not-start)
    (spark-state engine does-not-spark)
    (charge-state battery charged)
    (not repair advice ?a)
    =>
    (f/ask-yorno "Is the conductivity test for the ignition coil positive?"
                 'IGNI-POSIT))

  (determine-conductivity-test-1
    0
    ?q (asked question IGNI-POSIT)
    ?a (received answer ?a)
    =>
    (retract ?q ?a)
    (case ?a
      "yes" (asser repair advice "Repair the distributor lead wire.")
      "no"  (asser repair advice "Replace the ignition coil.")))

  (no-repairs
   -8
   (stage value diagnose)
   (not asked question ?q)
   (not repair advice ?a)
   =>
   (asser repair advice "Take your car to a mechanic."))

  (system-banner
    0
    ?s (stage value start)
    =>
    (f/println-browser "The expert system is asking questions..")
    (f/println-browser)
    (modify ?s value diagnose))

  (print-repair
    -8
    (repair advice ?a)
    =>
    (f/println-browser)
    (f/println-browser (str "Suggested Repair: " ?a))
    (f/println-browser))
))
