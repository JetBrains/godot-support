//  [Serializable]

using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace JetBrains.Rider.Godot.Editor
{
    struct ProtocolInstance
    {
        public string SolutionName;
        public int Port;

        public ProtocolInstance(string solutionName, int port)
        {
            SolutionName = solutionName;
            Port = port;
        }

        public static string ToJson(List<ProtocolInstance> connections)
        {
            //return JsonConvert.SerializeObject(connections); //turns out to be slow https://github.com/JetBrains/resharper-unity/issues/728 
            var sb = new StringBuilder("[");

            sb.Append(connections
                .Select(connection=> "{" + $"\"Port\":{connection.Port},\"SolutionName\":\"{connection.SolutionName}\",\"ProtocolGuid\":\"{ProtocolCompatibility.ProtocolGuid}\"" + "}")
                .Aggregate((a, b) => a + "," + b));

            sb.Append("]");
            return sb.ToString();
        }
    }
}