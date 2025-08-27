using ProjectApp.Data.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Shared.ViewModels
{
    public class AuctionVM
    {
        public Guid Id { get; set; } = Guid.NewGuid(); // Auto-generate GUID
        public string Name { get; set; }
        public string Description { get; set; }
        public string OwnerId { get; set; } // FK to ApplicationUser
        public decimal StartingPrice { get; set; }
        public DateTime EndTime { get; set; }

        public List<AuctionVM> lstAuctionViewModel { get; set; }
    }


    public class AuctionPostModel
    {
        public Guid Id { get; set; } = Guid.NewGuid(); // Auto-generate GUID
        public string Name { get; set; }
        public string Description { get; set; }
        public string OwnerId { get; set; } // FK to ApplicationUser
        public decimal StartingPrice { get; set; }
        public string EndTime { get; set; }

    }

    public class AuctionDetailVM
    {
       public Auction auctionDetail { get; set; }
    }

    public class BidPostModel
    {
        public string AuctionId { get; set; }

        public decimal BidAmount { get; set; }

    }


}
