using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Entity.Security
{
    internal class Roles
    {
        private string Id { get; set; }

        private string Username { get; set; }

        private string Password { get; set; }

        private string CreateAt { get; set; }

        private string UpdateAt { get; set; }

        private string DeleteAt { get; set; }

        private bool State { get; set; }
    }
}
