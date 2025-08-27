using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Data.Models
{
    public class Auction
    {
        public Guid Id { get; set; } = Guid.NewGuid(); // Auto-generate GUID
        public string Name { get; set; }
        public string Description { get; set; }
        public string OwnerId { get; set; } // FK to ApplicationUser
        public decimal StartingPrice { get; set; }
        public DateTime EndTime { get; set; }
        public ICollection<Bid> Bids { get; set; }

        [NotMapped]
        public string AuctionType { get; set; }
    }
}
