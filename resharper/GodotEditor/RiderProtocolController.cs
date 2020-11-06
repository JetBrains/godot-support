using System;
using JetBrains.Collections.Viewable;
using JetBrains.Diagnostics;
using JetBrains.Lifetimes;
using JetBrains.Rd.Impl;

namespace JetBrains.Rider.Godot.Editor
{
    public class RiderProtocolController
    {
        public readonly SocketWire.Server Wire;
        private static readonly ILog ourLogger = Log.GetLog<RiderProtocolController>();

        public RiderProtocolController(IScheduler mainThreadScheduler, Lifetime lifetime)
        {
            try
            {
                ourLogger.Verbose("Start ControllerTask...");

                Wire = new SocketWire.Server(lifetime, mainThreadScheduler, null, "GodotServer");
                Wire.BackwardsCompatibleWireFormat = true;
        
                ourLogger.Verbose($"Created SocketWire with port = {Wire.Port}");
            }
            catch (Exception ex)
            {
                ourLogger.Error("RiderProtocolController.ctor. " + ex);
            }
        }
    }
}