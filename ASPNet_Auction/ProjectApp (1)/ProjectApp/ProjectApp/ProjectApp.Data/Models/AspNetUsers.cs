using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Data.Models
{
    public class AspNetUsers
    {
        [Key]
        public Guid Id { get; set; } // User ID (usually a GUID or string)

        [Required]
        public string UserName { get; set; } // Username

        public string Email { get; set; } // Email (optional)

        // Add other columns from AspNetUsers table if needed
    }

}
