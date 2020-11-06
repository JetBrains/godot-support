using JetBrains.Application.Threading;
using JetBrains.Collections.Viewable;
using JetBrains.Diagnostics;
using JetBrains.DocumentModel;
using JetBrains.IDE;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.TextControl;
using JetBrains.Util;
using JetBrains.Util.dataStructures.TypedIntrinsics;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol.BackendGodot
{
    [SolutionComponent]
    public class PassthroughHost
    {
        private readonly ISolution mySolution;
        private readonly IThreading myThreading;
        private readonly IEditorManager myEditorManager;
        private readonly FrontendBackendHost myFrontendBackendHost;

        public PassthroughHost(Lifetime lifetime,
                               ISolution solution,
                               IThreading threading,
                               IEditorManager editorManager,
                               GodotReferencesTracker godotReferencesTracker,
                               BackendGodotHost backendGodotHost,
                               FrontendBackendHost frontendBackendHost)
        {
            mySolution = solution;
            myThreading = threading;
            myEditorManager = editorManager;
            myFrontendBackendHost = frontendBackendHost;

            godotReferencesTracker.HasGodotReference.View(lifetime, (godotProjectLifetime , args) =>
            {
                var model = frontendBackendHost.Model;
                if (args && model != null)
                {
                    // Advise the backend/Godot model as high priority so we can add our subscriptions first
                    using (Signal.PriorityAdviseCookie.Create())
                    {
                        backendGodotHost.BackendGodotModel.ViewNotNull(godotProjectLifetime,
                            AdviseGodotToFrontendModel);
                    }
                }
            });
        }

        private void AdviseGodotToFrontendModel(Lifetime lifetime, BackendGodotModel backendGodotModel)
        {
            // *********************************************************************************************************
            //
            // WARNING
            //
            // Be very careful with stateful properties!
            //
            // When the backend/godot protocol is closed, the existing properties maintain their current values. This
            // doesn't affect BackendGodotModel because we clear the model when the connection is lost. However, it does
            // affect any properties that have had values flowed in from BackedGodotModel - these values are not reset.
            //
            // When the backend/godot protocol is (re)created and advertised, we *should* have initial values from the
            // godot end (the model is advertised asynchronously to being created, and the dispatcher *should* have
            // processed messages). However, we cannot guarantee this - during testing, it usually works as expected,
            // but occasionally wouldn't be fully initialised. These means we need to be careful when assuming that
            // initial values are available in the properties. Advise and RdExtensions.FlowIntoRdSafe will correctly set
            // the target value if the source value exists. Avoid BeUtilExtensions.FlowIntoRd, as that will throw an
            // exception if the source value does not yet exist.
            // Note that creating and advertising the model, as well as all callbacks, happen on the main thread.
            //
            // We must ensure that the godot end (re)initialises properties when the protocol is created, or we could
            // have stale or empty properties here and in the frontend.
            //
            // *********************************************************************************************************

            var frontendBackendModel = myFrontendBackendHost.Model.NotNull("frontendBackendModel != null");
            AdviseOpenFile(backendGodotModel, frontendBackendModel);
        }

        private void AdviseOpenFile(BackendGodotModel backendGodotModel, FrontendBackendModel frontendBackendModel)
        {
            backendGodotModel.OpenFileLineCol.Set(args =>
            {
                var result = false;
                mySolution.Locks.ExecuteWithReadLock(() =>
                {
                    myEditorManager.OpenFile(FileSystemPath.Parse(args.Path), OpenFileOptions.DefaultActivate, myThreading,
                        textControl =>
                        {
                            var line = args.Line;
                            var column = args.Col;

                            if (line > 0 || column > 0) // avoid changing placement when it is not requested
                            {
                                if (line > 0) line--;
                                if (line < 0) line = 0;
                                if (column > 0) column--;
                                if (column < 0) column = 0;
                                textControl.Caret.MoveTo((Int32<DocLine>) line, (Int32<DocColumn>) column,
                                    CaretVisualPlacement.Generic);
                            }

                            frontendBackendModel.ActivateRider();
                            result = true;
                        },
                        () => result = false);
                });

                return result;
            });
        }
    }
}