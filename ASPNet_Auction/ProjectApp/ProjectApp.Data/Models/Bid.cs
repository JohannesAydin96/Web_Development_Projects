using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace ProjectApp.Data.Models
{
    public class Bid
    {
        public Guid Id { get; set; } = Guid.NewGuid(); // Auto-generate GUID for Bid

        // Foreign Key to Auction
        public Guid AuctionId { get; set; }

        // Navigation property to Auction
        public Auction Auction { get; set; } // This allows access to the related Auction object

        // Foreign Key to ApplicationUser (Bidder)
        public string UserId { get; set; }

        [NotMapped]
        public string UserName { get; set; }
        public decimal BidAmount { get; set; }
        public DateTime BidDateTime { get; set; }
    }
}
