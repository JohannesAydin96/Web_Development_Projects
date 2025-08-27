using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;

namespace ProjectApp.Shared.ViewModels
{
    public class BidVM
    {
        public Guid Id { get; set; } = Guid.NewGuid(); // Auto-generate GUID for Bid

        // Foreign Key to Auction
        public Guid AuctionId { get; set; }

        // Navigation property to Auction
        public AuctionVM Auction { get; set; } // This allows access to the related Auction object

        // Foreign Key to ApplicationUser (Bidder)
        public string UserId { get; set; }
        public IdentityUser User { get; set; } // Navigation property to ApplicationUser

        public decimal BidAmount { get; set; }
        public DateTime BidDateTime { get; set; }
    }
}
