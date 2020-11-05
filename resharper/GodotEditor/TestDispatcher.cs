using System;
using System.Collections.Generic;
using Godot;
using JetBrains.Collections.Viewable;
using JetBrains.Diagnostics;
using Thread = System.Threading.Thread;

namespace JetBrains.Rider.Godot.Editor
{
    public class TestDispatcher : IScheduler
    {
        private System.Timers.Timer myTimer;

        public void Start()
        {
            // Create a timer with a two second interval.
            myTimer = new System.Timers.Timer(200);
            // Hook up the Elapsed event for the timer. 
            myTimer.Elapsed += (_, __) =>
            {
                DispatchTasks();
            };
            myTimer.AutoReset = true;
            myTimer.Enabled = true;
        }

        private static Thread ourUIThread;
        internal static readonly TestDispatcher Instance = new TestDispatcher();

        /// <summary>
        /// The queue of tasks that are being requested for the next time DispatchTasks is called
        /// </summary>
        private readonly Queue<Action> myTaskQueue = new Queue<Action>();

        /// <summary>
        /// Dispatches the specified action delegate.
        /// </summary>
        /// <param name="action">Action  being requested</param>
        public void Queue(Action action)
        {
            if (Thread.CurrentThread == ourUIThread)
            {
                action();
                return;
            }

            lock (myTaskQueue)
            {
                myTaskQueue.Enqueue(action);
            }
        }

        /// <summary>
        /// Dispatches the tasks that has been requested since the last call to this function
        /// </summary>
        private void DispatchTasks()
        {
            ourUIThread = Thread.CurrentThread;

            if (myTaskQueue.Count == 0)
                return;
            while (true)
            {
                try
                {
                    if (myTaskQueue.Count == 0)
                        return;
                    var task = myTaskQueue.Dequeue();
                    task();
                }
                catch (Exception e)
                {
                    Log.GetLog<TestDispatcher>().Error(e);
                }
            }
        }

        public static void AssertThread()
        {
            Assertion.Require(ourUIThread == null || ourUIThread == Thread.CurrentThread, "Not a UI thread");
        }

        /// <summary>
        /// Indicates whether there are tasks available for dispatching
        /// </summary>
        /// <value>
        /// <c>true</c> if there are tasks available for dispatching; otherwise, <c>false</c>.
        /// </value>
        public bool IsActive => ourUIThread == null || ourUIThread == Thread.CurrentThread;

        public bool OutOfOrderExecution => false;
    }
}